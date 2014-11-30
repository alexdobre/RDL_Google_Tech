package com.therdl.client.callback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ErrorCodeMapper;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;

/**
 * This call back contains just the status OK or error
 */
public abstract class StatusCallback implements RequestCallback {

	private Beanery beanery = GWT.create(Beanery.class);
	private ValidatedView validatedView;

	protected StatusCallback(ValidatedView validatedView) {
		this.validatedView = validatedView;
	}

	@Override
	public void onResponseReceived(Request request, Response response) {
		Log.info("StatusCallback onResponseReceived: " + response.getText());
		if (response.getText().startsWith("c")) {
			//means we have an error code
			onErrorCodeReturned(response.getText());
		} else {
			onSuccess(request, response);
		}
	}

	public void onErrorCodeReturned(String errorCode) {
		Log.info("Error code returned " + errorCode);
		if (validatedView != null) {
			validatedView.setErrorMessage(ErrorCodeMapper.getI18nMessage(errorCode));
		}
	}

	public abstract void onSuccess(Request request, Response response);

	@Override
	public void onError(Request request, Throwable exception) {
		Log.info("ForgotPasswordPresenter onError)" + exception.getLocalizedMessage());
		onErrorCodeReturned(RDLConstants.ErrorCodes.GENERIC);
	}
}
