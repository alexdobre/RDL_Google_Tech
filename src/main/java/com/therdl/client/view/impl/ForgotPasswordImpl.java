package com.therdl.client.view.impl;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.ForgotPassword;
import com.therdl.client.view.widget.AppMenu;

/**
 * ForgotPasswordImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the RDL user created and voted features
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 */
public class ForgotPasswordImpl extends AbstractValidatedView implements ForgotPassword {

	interface ForgotPasswordUiBinder extends UiBinder<Widget, ForgotPasswordImpl> {
	}

	private static ForgotPasswordUiBinder ourUiBinder = GWT.create(ForgotPasswordUiBinder.class);

	private Presenter presenter;

	@UiField
	TextBox txtBoxEmail, txtUserName;

	@UiField
	Button btnSubmit;

	@UiField
	Modal forgotPassModal;

	public ForgotPasswordImpl() {
		ourUiBinder.createAndBindUi(this);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Modal getForgotPassModal() {
		return forgotPassModal;
	}

	@UiHandler("btnSubmit")
	public void onSubmitClicked(ClickEvent event) {
		hideMessages();
		String result = presenter.doForgotPassword(txtBoxEmail.getText(), txtUserName.getText());
		if (result != null){
			setErrorMessage(result);
		}
	}

	@Override
	public AppMenu getAppMenu() {
		return null;
	}
}