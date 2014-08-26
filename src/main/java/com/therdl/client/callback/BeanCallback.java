package com.therdl.client.callback;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ErrorCodeMapper;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;

/**
 * A call back containing one bean
 */
public abstract class BeanCallback<T> implements RequestCallback {

	protected static Logger log = Logger.getLogger(SnipListCallback.class.getName());

	private Beanery beanery = GWT.create(Beanery.class);
	private Class expectedClass;
	private ValidatedView validatedView;

	protected BeanCallback(Class expectedClass, ValidatedView validatedView) {
		this.expectedClass = expectedClass;
		this.validatedView = validatedView;
	}

	@Override
	public void onResponseReceived(Request request, Response response) {
		log.info("BeanCallback onResponseReceived: " + response.getText());
		if (!response.getText().startsWith("{")) {
			//means we have an error code
			onErrorCodeReturned(response.getText());
		} else {
			// de serialize the bean
			AutoBean<T> returnedBean = AutoBeanCodex.decode(beanery, expectedClass, response.getText());
			onBeanReturned(returnedBean);
		}
	}

	public void onErrorCodeReturned(String errorCode) {
		log.info("Error code returned " + errorCode);
		validatedView.setErrorMessage(ErrorCodeMapper.getI18nMessage(errorCode));
	}

	public abstract void onBeanReturned(AutoBean<T> returnedBean);

	@Override
	public void onError(Request request, Throwable exception) {
		log.info("ForgotPasswordPresenter onError)" + exception.getLocalizedMessage());
		onErrorCodeReturned(RDLConstants.ErrorCodes.GENERIC);
	}
}
