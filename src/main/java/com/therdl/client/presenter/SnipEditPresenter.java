package com.therdl.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.BeanCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.validation.SnipViewValidator;
import com.therdl.client.view.SnipEditView;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.SnipViewEvent;

/**
 * SnipEditPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 * @ SnipEditView  view this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ List<JSOModel> jSonList  as a JSON Array of search results used to obtain a snip for editing
 * JSOModel objects, see com.therdl.shared.beans.JSOModel javadoc for this class
 * @ String currentSnipId  used to retrieve the users correct snip
 */

public class SnipEditPresenter extends RdlAbstractPresenter<SnipEditView>
		implements SnipEditView.Presenter, ValueChangeHandler<String> {

	private String currentSnipId;
	private HasWidgets container;

	public SnipEditPresenter(SnipEditView view, AppController appController, String token) {
		super(appController);
		this.view = view;
		this.view.setPresenter(this);
		this.currentSnipId = RDLUtils.extractCurrentSnipId(token);
		Log.info("currentSnipId at constructor time: " + currentSnipId + " from token: " + token);
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		Log.info("SnipSearchPresenter go");
		this.container = container;
		checkLogin(null);
		container.clear();
		loadEditor();
		showHide();
	}

	private void showHide() {
		view.showHideCategories(false);
		view.showHideIdeaTypes(false);

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS) || Global.moduleName
				.equals(RDLConstants.Modules.SERVICES)) {
			//idea edit has categories and idea types
			view.showHideCategories(true);
			view.showHideIdeaTypes(true);
			view.showHideImprovementPanels(false);
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			//story edit has only categories
			view.showHideCategories(true);
			view.showHideIdeaTypes(false);
			view.showHideImprovementPanels(false);
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			//improvements edit has only improvements panels
			view.showHideCategories(false);
			view.showHideIdeaTypes(false);
			view.showHideImprovementPanels(true);
		}
	}

	/*
	 * loads snip editor for creating new snip when there is no selected snip
	 * or send request to find snip with the current snip id
	 */
	private void loadEditor() {
		if (currentSnipId != null && !currentSnipId.equals("")) {
			findSnipById(currentSnipId);
		} else {
			view.populate(null);
			container.add(view.asWidget());
		}
	}

	/*
	 * send request to edit selected snip
	 * @param bean snip bean to edit
	 */
	@Override
	public void submitEditedBean(AutoBean<SnipBean> bean, AutoBean<CurrentUserBean> currentUserBean) {
		String validationResult = SnipViewValidator.validateSnipBean(bean);
		if (validationResult != null) {
			view.setErrorMessage(validationResult);
			return;
		}
		grabSnipFunc.updateSnip(bean, currentUserBean.as().getToken(), new StatusCallback(view) {
			@Override
			public void onSuccess(Request request, Response response) {
				GuiEventBus.EVENT_BUS.fireEvent(new SnipViewEvent(response.getText()));
			}
		});
	}

	@Override
	public void onDeleteSnip(AutoBean<SnipBean> bean, AutoBean<CurrentUserBean> currentUserBean) {
		String id = bean.as().getId();
		Log.info("SnipEditPresenter onDelete: snip id " + id);
		String validationResult = SnipViewValidator.validateCanDelete(bean, currentUserBean.as().getName());
		if (validationResult != null) {
			view.setErrorMessage(validationResult);
			return;
		}
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;

		Log.info("SnipEditPresenter onDeleteSnip updateUrl: " + updateUrl);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json; charset=UTF-8");
		try {
			AutoBean<SnipBean> actionBean = beanery.snipBean();
			actionBean.as().setAction("delete");
			bean.as().setToken(currentUserBean.as().getToken());
			actionBean.as().setId(id);
			String json = AutoBeanCodex.encode(actionBean).getPayload();

			Log.info("SnipEditPresenter submit json: " + json);
			requestBuilder.sendRequest(json, new StatusCallback(view) {
				@Override
				public void onSuccess(Request request, Response response) {
					GuiEventBus.EVENT_BUS.fireEvent(new SnipViewEvent(response.getText()));
				}
			});
		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
	}

	/*
	 * send request to save new snip
	 * @param bean new snip bean
	 */
	@Override
	public void submitBean(AutoBean<SnipBean> bean, AutoBean<CurrentUserBean> currentUserBean) {
		String validationResult = SnipViewValidator.validateSnipBean(bean);
		if (validationResult != null) {
			view.setErrorMessage(validationResult);
			return;
		}
		Log.info("SnipEditPresenter submitBean bean : title : " + bean.as().getTitle());
		grabSnipFunc.createSnip(bean, currentUserBean.as().getToken(), new StatusCallback(view) {
			@Override
			public void onSuccess(Request request, Response response) {
				GuiEventBus.EVENT_BUS.fireEvent(new SnipViewEvent(response.getText()));
			}
		});

		bean.as().setAction(RDLConstants.SnipAction.SAVE);
		bean.as().setToken(currentUserBean.as().getToken());
	}

	private void findSnipById(String snipId) {
		Log.info("SnipEditPresenter findSnipById");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;

		Log.info("SnipEditPresenter findSnipById  updateUrl: " + updateUrl);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json; charset=UTF-8");
		AutoBean<SnipBean> currentBean = beanery.snipBean();
		currentBean.as().setAction("getSnip");
		currentBean.as().setId(snipId);

		String json = AutoBeanCodex.encode(currentBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new BeanCallback<SnipBean>(SnipBean.class, view) {
				@Override
				public void onBeanReturned(AutoBean<SnipBean> returnedBean) {
					view.populate(returnedBean);
					Log.info("View populate - END container: " + container);
					container.clear();
					container.add(view.asWidget());
				}
			});
		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
	}

	public SnipEditView getView() {
		return view;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
		Log.info("SnipEditPresenter  onValueChange" + stringValueChangeEvent.getValue());
		if (stringValueChangeEvent.getValue().equals("snips")) {

			if (!getController().getCurrentUserBean().as().isAuth()) {
				History.newItem(RDLConstants.Tokens.WELCOME);
				History.fireCurrentHistoryState();

			}
		}
	}
}
