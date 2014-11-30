package com.therdl.client.callback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * A call back containing a list of snips
 */
public abstract class SnipListCallback implements RequestCallback {

	private Beanery beanery = GWT.create(Beanery.class);

	@Override
	public void onResponseReceived(Request request, Response response) {
		JsArray<JSOModel> data =
				JSOModel.arrayFromJson(response.getText());

		ArrayList<JSOModel> jSonList = new ArrayList<JSOModel>();
		ArrayList<AutoBean<SnipBean>> beanList = new ArrayList<AutoBean<SnipBean>>();

		for (int i = 0; i < data.length(); i++) {
			jSonList.add(data.get(i));
			beanList.add(AutoBeanCodex.decode(beanery, SnipBean.class, jSonList.get(i).get(i + "")));
		}

		onBeanListReturned(beanList);
	}

	public abstract void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList);

	@Override
	public void onError(Request request, Throwable exception) {
		Log.info("SnipListCallback initialUpdate onError)" + exception.getLocalizedMessage());
	}

}
