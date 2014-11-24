package com.therdl.client.presenter.func.impl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.callback.SnipCallback;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * Functionality related to getting snips from the server
 */
public class GrabSnipFuncImpl implements GrabSnipFunc {

	protected Beanery beanery = GWT.create(Beanery.class);

	public void grabSnip(AutoBean<SnipBean> searchOptions, final SnipCallback snipCallback) {
		ViewUtils.populateDefaultSearchOptions(searchOptions);

		searchSnips(searchOptions, new SnipListCallback() {
			public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
				if (beanList != null && !beanList.isEmpty()) {
					snipCallback.onSnipBeanReturned(beanList.get(0));
				} else {
					snipCallback.onSnipBeanReturned(null);
				}
			}

		});
	}

	public void createSnip(AutoBean<SnipBean> createSnip, String token, StatusCallback statusCallback) {
		createSnip.as().setAction(RDLConstants.SnipAction.SAVE);
		createSnip.as().setToken(token);
		Log.info("GrabSnipFuncImpl save snip");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		try {
			String json = AutoBeanCodex.encode(createSnip).getPayload();
			Log.info("SnipEditPresenter submit json: " + json);
			requestBuilder.sendRequest(json, statusCallback);
		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
	}

	public void updateSnip(AutoBean<SnipBean> updatedSnip, String token, StatusCallback statusCallback) {
		updatedSnip.as().setAction(RDLConstants.SnipAction.UPDATE);
		updatedSnip.as().setToken(token);
		Log.info("GrabSnipFuncImpl update snip");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		try {
			String json = AutoBeanCodex.encode(updatedSnip).getPayload();
			Log.info("SnipEditPresenter submit json: " + json);
			requestBuilder.sendRequest(json, statusCallback);
		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
	}

	@Override
	public void searchSnips(final AutoBean<SnipBean> searchOptionsBean, RequestCallback callback) {
		Log.info("GrabSnipFunc searchSnips");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		searchOptionsBean.as().setAction(RDLConstants.SnipAction.SEARCH);
		Log.info("SnipSearchPresenter searchSnips: " + searchOptionsBean.as());

		String json = AutoBeanCodex.encode(searchOptionsBean).getPayload();
		try {
			requestBuilder.sendRequest(json, callback);
		} catch (RequestException e) {
			Log.error(e.getMessage(), e);
		}
	}

	@Override
	public void grabFaqList(SnipListCallback callback) {
		Log.info("GrabSnipFunc grabFaqList");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		searchOptionsBean.as().setAction(RDLConstants.SnipAction.GET_FAQ);
		Log.info("SnipSearchPresenter searchSnips: " + searchOptionsBean.as());

		String json = AutoBeanCodex.encode(searchOptionsBean).getPayload();
		try {
			requestBuilder.sendRequest(json, callback);
		} catch (RequestException e) {
			Log.error(e.getMessage(), e);
		}
	}

	/**
	 * gives reputation to the current snip, increments reputation counter and saves user id to ensure giving reputation per user/snip only once
	 */
	@Override
	public void giveSnipReputation(String id, AutoBean<CurrentUserBean> currentUserBean, ValidatedView validatedView,
			final RequestObserver observer) {
		Log.info("GrabSnipFunc giveSnipReputation id=" + id);
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		AutoBean<SnipBean> currentBean = beanery.snipBean();
		currentBean.as().setAction("giveRep");
		currentBean.as().setId(id);
		currentBean.as().setToken(currentUserBean.as().getToken());
		currentBean.as().setAuthor(currentUserBean.as().getName());

		String json = AutoBeanCodex.encode(currentBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new StatusCallback(validatedView) {
				@Override
				public void onSuccess(Request request, Response response) {
					observer.onSuccess(response.getText());
				}
			});
		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
	}
}
