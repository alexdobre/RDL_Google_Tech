package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.BeanCallback;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.handler.LoginHandler;
import com.therdl.client.app.FuncFactory;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.view.RdlView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AbuseCommentsPopup;
import com.therdl.client.view.widget.SupportRdlPopup;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.CredentialsSubmitEvent;
import com.therdl.shared.events.CredentialsSubmitEventHandler;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogInOkEvent;
import com.therdl.shared.events.LoginFailEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the super class for all presenters. It contains common logic.
 * At the time of writing I'm placing the cookie login logic here
 * Created by Alex on 11/02/14.
 */
public abstract class RdlAbstractPresenter<T extends RdlView> implements CommonPresenter {

	protected static Logger log = Logger.getLogger(RdlAbstractPresenter.class.getName());
	protected AppController controller;
	protected Beanery beanery = GWT.create(Beanery.class);
	protected T view;
	protected GrabSnipFunc grabSnipFunc;

	public RdlAbstractPresenter(AppController controller) {
		this.controller = controller;
		GuiEventBus.EVENT_BUS.addHandler(CredentialsSubmitEvent.TYPE, new CredentialsSubmitEventHandler() {
			@Override
			public void onCredentialsSubmitEvent(CredentialsSubmitEvent event) {
				doLogIn(event.getEmailTxt(), event.getPasswordText(),
						event.getRememberMe(), event.getSid(), event.getLoginHandler());
			}
		});
		grabSnipFunc = FuncFactory.createGrabSnipFunc();
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void checkLogin() {
		log.info("RDL abstract presenter check login is auth: " + getController().getCurrentUserBean().as().isAuth());
		if (getController().getCurrentUserBean().as().isAuth()) {
			//do nothing
		} else {
			loginCookieCheck();
		}
	}

	/**
	 * Checks if the SID cookie is active and logs in if it is
	 */
	private void loginCookieCheck() {
		if (!controller.getCurrentUserBean().as().isAuth()) {
			log.info("RdlAbstractPresenter loginCookieCheck");
			//check the cookie
			String sessionID = Cookies.getCookie("sid");
			if (sessionID != null) {
				log.info("Found cookie with SID " + sessionID);
				//check if the SID is found and authenticate the user
				doLogIn(null, null, true, sessionID, null);
			} else {
				view.getAppMenu().logOut();
			}
		}
	}

	/**
	 * calls com.therdl.server.restapi.SessionServlet class to authorise user from database, creates
	 * a AutoBean<AuthUserBean> authBean from the users credentials and submits it as a json serialised object
	 * calls AppController controller and  WelcomeView  welcomeView objects
	 * controller.setCurrentUserBean(name, email,  auth)::  sets the authorisation state based on
	 * authorise user from database result
	 *
	 * @param emailTxt     String unique identifier for login and subsequent granted state information
	 * @param passwordText String password identifier for login
	 * @param loginHandler loginHandler which is called when login is successful
	 */
	public void doLogIn(String emailTxt, String passwordText, Boolean rememberMe, String sid, final LoginHandler loginHandler) {

		log.info("RdlAbstractPresenter doLogIn BEGIN  emailTxt  " + emailTxt + " sid: " + sid);
		//used in inner class logic
		final Boolean innerRememberMe = rememberMe;
		final Boolean innerIsCookieLogin = (emailTxt == null);

		String authUrl = GWT.getModuleBaseURL() + "getSession";

		log.info("RdlAbstractPresenter submit updateUrl: " + authUrl);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(authUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		try {
			AutoBean<AuthUserBean> authBean = beanery.authBean();
			if (sid != null && emailTxt == null) {
				authBean.as().setSid(sid);
				authBean.as().setAction("sidAuth");
			} else {
				authBean.as().setPassword(passwordText);
				authBean.as().setEmail(emailTxt);
				authBean.as().setRememberMe(rememberMe);
				authBean.as().setAction("auth");
			}
			String json = AutoBeanCodex.encode(authBean).getPayload();

			log.info("RdlAbstractPresenter submit json: " + json);
			requestBuilder.sendRequest(json, new BeanCallback<AuthUserBean>(AuthUserBean.class, null) {

				@Override
				public void onBeanReturned(AutoBean<AuthUserBean> returnedBean) {
					controller.setCurrentUserBean(returnedBean.as().getName(), returnedBean.as().getEmail(),
							true, returnedBean.as().getTitles(), returnedBean.as().getIsRDLSupporter(),
							returnedBean.as().getToken(), returnedBean.as().getRep(), returnedBean.as().getDateCreated());

					sidLogic(returnedBean);

					// try and update any open view
					controller.getCurrentUserBean().as().setAuth(true);
					GuiEventBus.EVENT_BUS.fireEvent(new LogInOkEvent(controller.getCurrentUserBean()));

					if (loginHandler != null) {
						log.info("loginHandler != null");
						loginHandler.onSuccess(controller.getCurrentUserBean());
					}
				}

				private void sidLogic(AutoBean<AuthUserBean> returnedBean) {
					//if this was not a cookie login we do logic to change the cookie if necessary
					if (!innerIsCookieLogin) {
						log.info("Was not a cookie log in - performing SID logic");
						//if there is no cookie and remember me was set we create a new cookie
						if (innerRememberMe) {
							log.info("RememberMe = true and SID cookie null -> setting new cookie with sid: " + returnedBean.as().getSid());
							//set session cookie for 14 day expiry.
							String sessionID = returnedBean.as().getSid();
							final long DURATION = 1000 * 60 * 60 * 24 * 14;
							Date expires = new Date(System.currentTimeMillis() + DURATION);
							Cookies.setCookie("sid", sessionID, expires, null, "/", false);
						}//if the user unchecks the RememberMe box then we remove the cookie
						else {
							log.info("RememberMe = false -> removing cookie");
							Cookies.removeCookie("sid");
						}
					}
				}

				@Override
				public void onErrorCodeReturned(String errorCode) {
					log.info("Login failed - firing event with code: " + errorCode);
					Cookies.removeCookie("sid");
					GuiEventBus.EVENT_BUS.fireEvent(new LoginFailEvent(errorCode));
				}

			});
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

	public void searchAbuse(AutoBean<SnipBean> searchOptions, RequestCallback callback) {
		log.info("SnipSearchPresenter getSnipSearchResult");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		searchOptions.as().setAction("searchAbuse");
		log.info("SnipSearchPresenter searchSnips: " + searchOptions.as());

		String json = AutoBeanCodex.encode(searchOptions).getPayload();
		try {
			requestBuilder.sendRequest(json, callback);
		} catch (RequestException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Searches for and returns the RDL Supporter title description from the DB
	 *
	 * @param supportRdlPopup the popup to set the content in
	 */
	@Override
	public void grabRdlSupporterTitleDesc(final SupportRdlPopup supportRdlPopup) {
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		ViewUtils.populateDefaultSearchOptions(searchOptionsBean);
		searchOptionsBean.as().setCoreCat(CoreCategory.GENERAL.getShortName());
		searchOptionsBean.as().setTitle(RDLConstants.ContentMgmt.RDL_SUPP_TITLE);
		searchOptionsBean.as().setAuthor(RDLConstants.ContentMgmt.OFFICIAL_AUTHOR);
		searchOptionsBean.as().setSnipType(SnipType.CONTENT_MGMT.getSnipType());

		grabSnipFunc.searchSnips(searchOptionsBean, new SnipListCallback() {

			public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
				supportRdlPopup.populateBody(beanList.get(0));
			}

		});
	}

	@Override
	public void reportAbuse(String contentId, String reason) {
		log.info("Report abuse on item: " + contentId + " reason: " + reason);
		log.info("SnipSearchPresenter getSnipSearchResult");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		AutoBean<SnipBean> actionBean = beanery.snipBean();
		actionBean.as().setAction(RDLConstants.SnipAction.REPORT_ABUSE);
		actionBean.as().setParentSnip(contentId);
		actionBean.as().setContent(reason);
		actionBean.as().setAuthor(getController().getCurrentUserBean().as().getName());
		actionBean.as().setToken(getController().getCurrentUserBean().as().getToken());

		String json = AutoBeanCodex.encode(actionBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new StatusCallback(null) {
				@Override
				public void onSuccess(Request request, Response response) {
					log.info("report abuse on success");
					getController().getAppMenu().getReportAbusePopup().hide();
					History.fireCurrentHistoryState();
				}
			});
		} catch (RequestException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void showAbuseComments(SnipBean abusiveContent, final AbuseCommentsPopup popup) {

		log.info("Retrieve abuse comments for: " + abusiveContent);
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		AutoBean<SnipBean> actionBean = beanery.snipBean();
		ViewUtils.populateDefaultSearchOptions(actionBean);
		actionBean.as().setAction("searchAbuse");
		actionBean.as().setParentSnip(abusiveContent.getId());
		actionBean.as().setSnipType(SnipType.ABUSE_REPORT.getSnipType());
		actionBean.as().setToken(getController().getCurrentUserBean().as().getToken());

		String json = AutoBeanCodex.encode(actionBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new SnipListCallback() {

				public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
					List<String> contentList = new ArrayList<>(beanList.size());
					for (AutoBean<SnipBean> returnedBean : beanList) {
						contentList.add(returnedBean.as().getContent());
					}
					popup.show();
					popup.populateBody(contentList);
				}

			});
		} catch (RequestException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public AppController getController() {
		return controller;
	}
}
