package com.therdl.client.presenter.func.impl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.callback.SnipCallback;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.SnipViewEvent;

/**
 * Functionality related to getting snips from the server
 */
public class GrabSnipFuncImpl implements GrabSnipFunc {
	protected static Logger log = Logger.getLogger(GrabSnipFuncImpl.class.getName());

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
		log.info("GrabSnipFuncImpl save snip");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		try {
			String json = AutoBeanCodex.encode(createSnip).getPayload();
			log.info("SnipEditPresenter submit json: " + json);
			requestBuilder.sendRequest(json, statusCallback);
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

	public void updateSnip(AutoBean<SnipBean> updatedSnip, String token, StatusCallback statusCallback) {
		updatedSnip.as().setAction(RDLConstants.SnipAction.UPDATE);
		updatedSnip.as().setToken(token);
		log.info("GrabSnipFuncImpl update snip");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		try {
			String json = AutoBeanCodex.encode(updatedSnip).getPayload();
			log.info("SnipEditPresenter submit json: " + json);
			requestBuilder.sendRequest(json, statusCallback);
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

	@Override
	public void searchSnips(final AutoBean<SnipBean> searchOptionsBean, RequestCallback callback) {
		log.info("SnipSearchPresenter getSnipSearchResult");
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		searchOptionsBean.as().setAction("search");
		log.info("SnipSearchPresenter searchSnips: " + searchOptionsBean.as());

		String json = AutoBeanCodex.encode(searchOptionsBean).getPayload();
		try {
			requestBuilder.sendRequest(json, callback);
		} catch (RequestException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
