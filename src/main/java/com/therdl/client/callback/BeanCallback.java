package com.therdl.client.callback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ErrorCodeMapper;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;

/**
 * A call back containing one bean
 */
public abstract class 		BeanCallback<T> implements RequestCallback {

	private Beanery beanery = GWT.create(Beanery.class);
	private Class expectedClass;
	private ValidatedView validatedView;

	protected BeanCallback(Class expectedClass, ValidatedView validatedView) {
		this.expectedClass = expectedClass;
		this.validatedView = validatedView;
	}

	@Override
	public void onResponseReceived(Request request, Response response) {
		Log.info("BeanCallback onResponseReceived: " + response.getText());
		if (response.getText().startsWith("c")) {
			//means we have an error code
			onErrorCodeReturned(response.getText());
		} else if (response.getText() == null || response.getText().equals("")) {
			//means content not found
			History.newItem(RDLConstants.Tokens.ERROR);
		} else {
			// de serialize the bean
			AutoBean<T> returnedBean = AutoBeanCodex.decode(beanery, expectedClass, response.getText());
			onBeanReturned(returnedBean);
		}
	}

	public void onErrorCodeReturned(String errorCode) {
		Log.info("Error code returned " + errorCode);
		validatedView.setErrorMessage(ErrorCodeMapper.getI18nMessage(errorCode));
	}

	public abstract void onBeanReturned(AutoBean<T> returnedBean);

	@Override
	public void onError(Request request, Throwable exception) {
		Log.info("ForgotPasswordPresenter onError)" + exception.getLocalizedMessage());
		onErrorCodeReturned(RDLConstants.ErrorCodes.GENERIC);
	}
}
