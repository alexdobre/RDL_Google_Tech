package com.therdl.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.RDL;
import com.therdl.client.callback.BeanCallback;
import com.therdl.client.validation.UserViewValidator;
import com.therdl.client.view.ForgotPassword;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;

/**
 * ForgotPasswordPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 */

public class ForgotPasswordPresenter implements ForgotPassword.Presenter {

	private Beanery beanery = GWT.create(Beanery.class);
	private ForgotPassword view;

	public ForgotPasswordPresenter(ForgotPassword forgotPassword) {
		this.view = forgotPassword;
		this.view.setPresenter(this);
	}

	@Override
	public String doForgotPassword(String email, String userName) {
		AutoBean<AuthUserBean> bean = beanery.authBean();
		String forgotUrl = GWT.getModuleBaseURL() + "getSession";
		bean.as().setEmail(email);
		bean.as().setName(userName);
		bean.as().setPassword("forgot_password");
		bean.as().setPasswordConfirm("forgot_password");
		bean.as().setAction("forgotPass");

		//validate the bean
		String validationResult = UserViewValidator.validateAuthUserBean(bean);
		if (validationResult != null)
			return validationResult;

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(forgotUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		String json = AutoBeanCodex.encode(bean).getPayload();

		try {
			requestBuilder.sendRequest(json, new BeanCallback<AuthUserBean>(AuthUserBean.class, view) {
				@Override
				public void onBeanReturned(AutoBean<AuthUserBean> authUserBean) {
					view.setSuccessMessage(RDL.getI18n().newPasswordSentToEmail());
				}

			});
		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
		return null;
	}

	@Override
	public void showForgotPasswordPopup() {
		view.getForgotPassModal().show();
	}

}