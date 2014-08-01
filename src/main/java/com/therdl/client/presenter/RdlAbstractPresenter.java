package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.RdlView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.Constants;
import com.therdl.shared.LoginHandler;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogInOkEvent;

import java.util.Date;
import java.util.logging.Logger;

/**
 * This is the super class for all presenters. It contains common logic.
 * At the time of writing I'm placing the cookie login logic here
 * Created by Alex on 11/02/14.
 */
public abstract class RdlAbstractPresenter<T extends RdlView> implements Presenter {

	protected static Logger log = Logger.getLogger("");

	protected AppController controller;

	protected Beanery beanery = GWT.create(Beanery.class);

	protected T view;

	public RdlAbstractPresenter(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void checkLogin(AppMenu appMenu, AutoBean<CurrentUserBean> currentUserBean) {
		loginCookieCheck();
		// user must be authorised to edit
		if (getController().getCurrentUserBean().as().isAuth()) {
			log.info("!controller.getCurrentUserBean().as().isAuth()  ");
			appMenu.setLogOutVisible(true);
			appMenu.setSignUpVisible(false);
			appMenu.setUserInfoVisible(true);
			appMenu.setLoginResult(getController().getCurrentUserBean().as().getName(),
					getController().getCurrentUserBean().as().getEmail(), true);
		}else {
			appMenu.logOut();
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
			}
		}
	}

	/**
	 * Processing done if login fails
	 */
	private void loginFail() {
	}

	/**
	 * Processing done at login success
	 */
	private void loginSuccess() {
	}

	/**
	 * calls com.therdl.server.restapi.SessionServlet class to authorise user from database, creates
	 * a AutoBean<AuthUserBean> authBean from the users credentials and submits it as a json serialised object
	 * calls AppController controller and  WelcomeView  welcomeView objects
	 * controller.setCurrentUserBean(name, email, avatarUrl,  auth)::  sets the authorisation state based on
	 * authorise user from database result
	 * welcomeView.setloginresult(name, email, auth):: updatses the view with credentials mainly for the
	 * upper menu
	 *
	 * @param emailTxt     String unique identifier for login and subsequent granted state information
	 * @param passwordText String password identifier for login
	 * @param loginHandler loginHandler which is called when login is successful
	 */
	public void doLogIn(String emailTxt, String passwordText, Boolean rememberMe, String sid, final LoginHandler loginHandler) {

		log.info("RdlAbstractPresenter doLogIn BEGIN  emailTxt  " + emailTxt + " sid: " + sid);
		//used in inner class logic
		final Boolean innerRememberMe = rememberMe;
		final Boolean innerIsCookieLogin = emailTxt == null;

		String authUrl = GWT.getModuleBaseURL() + "getSession";

		if (!Constants.DEPLOY) {
			authUrl = authUrl.replaceAll("/therdl", "");
		}

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
			requestBuilder.sendRequest(json, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {

					if (response.getStatusCode() == 200) {
						// ok move forward
						log.info("RdlAbstractPresenter onSubmit post ok");
						log.info("RdlAbstractPresenter onSubmit onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
						log.info("RdlAbstractPresenter onSubmit onResponseReceived json" + response.getText());
						JSOModel data = JSOModel.fromJson(response.getText());

						AutoBean<AuthUserBean> authUserBean = AutoBeanCodex.decode(beanery, AuthUserBean.class, response.getText());

						String email = data.get("email");
						String name = data.get("name");
						boolean isRDLSupporter = data.getBoolean("isRDLSupporter");

						boolean auth = data.getBoolean("auth");
						String avatarUrl = null;
						if (!auth) {
							log.info("RdlAbstractPresenter onResponseReceived  LoginFail!  ");
							loginFail();
							if (innerIsCookieLogin) {
								log.info("SID login fail -> cleaning up cookie");
								Cookies.removeCookie("sid");
							}
							controller.setCurrentUserBean("", "", avatarUrl, auth, null, isRDLSupporter);
						} else {
							if (data.get("name") != null) {
								log.info("RdlAbstractPresenter  avatarUrl exists" + data.get("avatarUrl"));
								avatarUrl = data.get("avatarUrl");
							}
							controller.setCurrentUserBean(name, email, avatarUrl, auth, authUserBean.as().getTitles(), isRDLSupporter);

							//if this was not a cookie login we do logic to change the cookie if necessary
							if (!innerIsCookieLogin) {
								//if there is no cookie and remember me was set we create a new cookie
								if (innerRememberMe && Cookies.getCookie("sid") == null) {
									log.info("RememberMe = true and SID cookie null -> setting new cookie with sid: " + data.get("sid"));
									//set session cookie for 14 day expiry.
									String sessionID = data.get("sid");
									final long DURATION = 1000 * 60 * 60 * 24 * 14;
									Date expires = new Date(System.currentTimeMillis() + DURATION);
									Cookies.setCookie("sid", sessionID, expires, null, "/", false);
								}//if the user unchecks the RememberMe box then we remove the cookie
								else if (!innerRememberMe) {
									log.info("RememberMe = false -> removing cookie");
									Cookies.removeCookie("sid");
								}
							}

							// try and update any open view
							controller.getCurrentUserBean().as().setAuth(true);
							GuiEventBus.EVENT_BUS.fireEvent(new LogInOkEvent(controller.getCurrentUserBean()));
							loginSuccess();

							if (loginHandler != null) {
								log.info("loginHandler != null");
								loginHandler.onSuccess(controller.getCurrentUserBean());

							}
						}
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					log.info("SignInViewImpl onSubmit onError)" + exception.getLocalizedMessage());
				}

			});
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

	/**
	 * Returns the range of items in the list to be displayed based on the default page length
	 *
	 * @param listSize  the current list on the page
	 * @param pageIndex the current page index starting with 1
	 * @return the page range in the format 1-50
	 */
	public String calculateListRange(int listSize, int pageIndex) {
		if (listSize == 0) return "0";

		int startIndex = pageIndex * Constants.DEFAULT_PAGE_SIZE + 1;
		int stopIndex = startIndex + listSize - 1;
		return startIndex + "-" + stopIndex;
	}

	public AppController getController() {
		return controller;
	}

}
