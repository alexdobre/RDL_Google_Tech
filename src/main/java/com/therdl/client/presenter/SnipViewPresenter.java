package com.therdl.client.presenter;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.BeanCallback;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.handler.ClickHandler;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * SnipViewPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 * @ SnipView  snipView this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ String currentSnipId  used to retrieve the users correct snip
 */
public abstract class SnipViewPresenter extends RdlAbstractPresenter<SnipView> implements Presenter, SnipView.Presenter {

	private static Logger log = Logger.getLogger(SnipViewPresenter.class.getName());

	protected Beanery beanery = GWT.create(Beanery.class);
	protected String currentSnipId;
	protected AutoBean<CurrentUserBean> currentUserBean;
	protected AutoBean<SnipBean> searchOptionsBean;
	protected HasWidgets container;
	protected AutoBean<SnipBean> currentSnipBean;

	public SnipViewPresenter(SnipView snipView, AppController appController, String token) {
		super(appController);
		this.view = snipView;
		this.currentSnipId = RDLUtils.extractCurrentSnipId(token);
		log.info("currentSnipId at constructor time: " + currentSnipId + " from token: " + token);
		this.controller = appController;
		this.view.setPresenter(this);
		this.searchOptionsBean = RDLUtils.parseSearchToken(beanery, token, currentSnipId);
		snipView.setSearchOptionsBean(searchOptionsBean);
	}

	/**
	 * standard runtime method for MVP architecture
	 *
	 * @param container       the view container
	 * @param currentUserBean the user state bean, mainly used for authorisation
	 *                        and to update the menu
	 */
	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		this.container = container;
		checkLogin();
		this.currentUserBean = currentUserBean;
		container.clear();
		viewSnipById();
	}

	public void editSnip() {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
			History.newItem(RDLConstants.Tokens.SNIP_EDIT + ":" + currentSnipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
			History.newItem(RDLConstants.Tokens.THREAD_EDIT + ":" + currentSnipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
			History.newItem(RDLConstants.Tokens.PROPOSAL_EDIT + ":" + currentSnipBean.as().getId());

	}

	/**
	 * find snip for the currentSnipId, while not a parameter the following variable
	 * requires some explanation to help new developers
	 * JSOModel data is a utility class for mapping javascript data objects and arrays
	 * and storing them as a container to be used in a GWT java context
	 * this is a home grown utility class written to encapsulate standard javascript to java
	 * boiler plate code to keep main java code cleaner and more maintainable
	 */
	private void viewSnipById() {
		log.info("SnipViewPresenter viewSnipById currentSnipId=" + currentSnipId);
		String updateUrl = GWT.getModuleBaseURL() + "getSnips";

		log.info("SnipViewPresenter viewSnipById  updateUrl: " + updateUrl);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		AutoBean<SnipBean> currentBean = beanery.snipBean();
		currentBean.as().setAction("populateSnip");
		currentBean.as().setId(currentSnipId);

		//if the user is logged in and if we are in the IDEAS or IMPROVEMENTS module we need to know if the user has
		//given a reply so we set the viewer ID to be used in the search
		if (controller.getCurrentUserBean().as().isAuth()) {
			if (Global.moduleName.equals(RDLConstants.Modules.IDEAS) ||
					Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
				currentBean.as().setViewerId(controller.getCurrentUserBean().as().getName());
			}
		}

		String json = AutoBeanCodex.encode(currentBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new BeanCallback<SnipBean>(SnipBean.class, view) {
				@Override
				public void onBeanReturned(AutoBean<SnipBean> returnedBean) {
					if (returnedBean == null) {
						History.newItem(RDLConstants.Tokens.ERROR);
					} else {
						currentSnipBean = returnedBean;
						view.populateSnip(returnedBean);
						replyButtonLogic();
						snipActionLogic(returnedBean);
						container.add(view.asWidget());
					}
				}
			});
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

	protected void replyButtonLogic() {
		//if the user is not logged in we do not show the reply button
		if (currentUserBean == null || !currentUserBean.as().isAuth()) {
			view.showHideReplyButton(false);
		} else {
			boolean isAuthor = ViewUtils.isAuthor(currentUserBean, currentSnipBean);
			showReplyIfAuthor(isAuthor);
		}
	}

	protected abstract void showReplyIfAuthor(boolean isAuthor);

	protected void snipActionLogic(AutoBean<SnipBean> returnedBean) {
		boolean isAuthor = ViewUtils.isAuthor(currentUserBean, returnedBean);
		boolean repGiven = returnedBean.as().getIsRepGivenByUser() == 1;
		log.info("snipActionLogic isAuthor: " + isAuthor + " repGiven: " + repGiven);
		//if the user is not logged in we do not show any action
		if (currentUserBean == null || !currentUserBean.as().isAuth()) {
			view.hideSnipAction();
			return;
		}
		//the user is author so we show the edit button
		if (isAuthor) {
			view.showSnipAction(true, new ClickHandler() {
				@Override
				public void onClick(Object source) {
					//we do not place validation here but we do place it on the server side
					editSnip();
				}
			});
			//the user has not logged in or has not given rep so we show the give rep button
		} else if (!repGiven) {
			view.showSnipAction(false, new ClickHandler() {
				@Override
				public void onClick(Object source) {
					//we do not place validation here but we do place it on the server side
					giveSnipReputation(currentSnipId, new RequestObserver() {
						@Override
						public void onSuccess(String response) {
							currentSnipBean.as().setRep(currentSnipBean.as().getRep() + 1);
							view.populateSnip(currentSnipBean);
							view.showSnipAction(null, null);
						}
					});
				}
			});
			//we show the rep given icon
		} else {
			view.showSnipAction(null, null);
		}
	}

	/**
	 * send a request to the server to save reference for current snip
	 *
	 * @param bean representing reference object
	 */
	public void saveReference(AutoBean<SnipBean> bean) {
		log.info("SnipViewPresenter submit reference to server");
		bean.as().setAction("saveReference");
		final String refType = bean.as().getReferenceType();
		final String snipType = bean.as().getSnipType();
		String updateUrl = GWT.getModuleBaseURL() + "getSnips";

		log.info("SnipViewPresenter submit updateUrl: " + updateUrl);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		try {
			String json = AutoBeanCodex.encode(bean).getPayload();
			log.info("SnipViewPresenter submit json: " + json);
			requestBuilder.sendRequest(json, new StatusCallback(view) {
				@Override
				public void onSuccess(Request request, Response response) {
					view.saveReferenceResponseHandler(refType, snipType);
				}
			});
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

	@Override
	public void populateReplies(AutoBean<SnipBean> searchOptionsBean) {
		log.info("SnipViewPresenter populateReplies currentSnipId=" + currentSnipId);
		String updateUrl = GWT.getModuleBaseURL() + "getSnips";
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		searchOptionsBean.as().setAction("getReferences");
		searchOptionsBean.as().setId(currentSnipId);
		final int pageIndex = searchOptionsBean.as().getPageIndex();

		String json = AutoBeanCodex.encode(searchOptionsBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new SnipListCallback() {
				@Override
				public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
					view.showReferences(beanList, pageIndex);
					PaginationHelper.showPaginationOnView(pageIndex, beanList.size(), view);
				}
			});
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

	/**
	 * gives reputation to the current snip, increments reputation counter and saves user id to ensure giving reputation per user/snip only once
	 */

	public void giveSnipReputation(String id, final RequestObserver observer) {
		log.info("SnipViewPresenter giveSnipReputation id=" + id);
		String updateUrl = GWT.getModuleBaseURL() + "getSnips";

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		AutoBean<SnipBean> currentBean = beanery.snipBean();
		currentBean.as().setAction("giveRep");
		currentBean.as().setId(id);
		currentBean.as().setToken(currentUserBean.as().getToken());
		currentBean.as().setAuthor(currentUserBean.as().getName());

		String json = AutoBeanCodex.encode(currentBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new StatusCallback(view) {
				@Override
				public void onSuccess(Request request, Response response) {
					observer.onSuccess(response.getText());
				}
			});
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}
}
