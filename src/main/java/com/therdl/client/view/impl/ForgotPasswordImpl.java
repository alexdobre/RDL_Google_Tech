package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
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
public class ForgotPasswordImpl extends PopupPanel implements ForgotPassword {
	interface ForgotPasswordUiBinder extends UiBinder<Widget, ForgotPasswordImpl> {
	}

	private static ForgotPasswordUiBinder ourUiBinder = GWT.create(ForgotPasswordUiBinder.class);

	private Presenter presenter;

	@UiField
	org.gwtbootstrap3.client.ui.TextBox txtBoxEmail;

	@UiField
	Label lblEmailNotFound;

	@UiField
	org.gwtbootstrap3.client.ui.Button btnSubmit;

	@UiField
	org.gwtbootstrap3.client.ui.Modal modalSuccessResetPassword;

	@UiField
	org.gwtbootstrap3.client.ui.Modal modalFailResetPassword;

	public ForgotPasswordImpl() {
		super(true);
		add(ourUiBinder.createAndBindUi(this));
		lblEmailNotFound.setText("");
	}

	@Override
	public PopupPanel getForgotPasswordPopup() {
		return this;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasText getLabelEmailNotFound() {
		return lblEmailNotFound;
	}

	@Override
	public org.gwtbootstrap3.client.ui.Button getSubmitButton() {
		return btnSubmit;
	}

	@Override
	public org.gwtbootstrap3.client.ui.Modal getModalSuccessResetPassword() {
		return modalSuccessResetPassword;
	}

	@Override
	public org.gwtbootstrap3.client.ui.Modal getModalFailResetPasswprd() {
		return modalFailResetPassword;
	}

	@UiHandler("btnSubmit")
	public void onSubmitClicked(ClickEvent event) {
		String email = txtBoxEmail.getText().trim();
		if (!email.isEmpty()) {
			if (presenter != null) {
				presenter.doForgotPassword(email);
			}
			btnSubmit.setEnabled(false);
		}
	}

	/**
	 * get the email string
	 *
	 * @param event Standard GWT ClickEvent
	 */
	@UiHandler("txtBoxEmail")
	public void onEmailFocused(FocusEvent event) {
		lblEmailNotFound.setText("");
	}

	@Override
	public AppMenu getAppMenu(){
		return null;
	}
}