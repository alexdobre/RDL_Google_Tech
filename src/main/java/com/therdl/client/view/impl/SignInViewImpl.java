package com.therdl.client.view.impl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.SignInView;
import com.therdl.client.view.common.ErrorCodeMapper;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.events.*;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;

/**
 * SignInViewImpl extends PopupPanel is a pop up embedded in the Welcome view
 * fields below are standard GWT form fields for user sign-up
 *
 * @ TextBox  email  String used as the unique identifier in the database
 * @ PasswordTextBox psswd  String the user password
 */

public class SignInViewImpl extends AbstractValidatedView implements SignInView {

	private final AppMenu appMenu;

	private static SignInViewImplUiBinder uiBinder = GWT.create(SignInViewImplUiBinder.class);

	@UiField
	Modal modal;

	@UiField
	Input password;

	@UiField
	TextBox email;

	@UiField
	Button submit;

	@UiField
	CheckBox rememberMe;

	interface SignInViewImplUiBinder extends UiBinder<Widget, SignInViewImpl> {
	}

	public SignInViewImpl(AppMenu appMenu) {
		initWidget(uiBinder.createAndBindUi(this));
		this.appMenu = appMenu;
		password.setText("password");
		email.setText("Email");
		this.setStyleName("signInView");
		rememberMe.setValue(true);

		// user has just successfully logged in update app menu
		GuiEventBus.EVENT_BUS.addHandler(LogInOkEvent.TYPE, new LogInOkEventEventHandler() {

			@Override
			public void onLogInOkEvent(LogInOkEvent onLoginOkEvent) {
				hideMessages();
				modal.hide();
			}
		});

		// user has just successfully logged in update app menu
		GuiEventBus.EVENT_BUS.addHandler(LoginFailEvent.TYPE, new LoginFailEventHandler() {

			@Override
			public void onLogFailEvent(LoginFailEvent loginFailEvent) {
				Log.info("Sign in view setting error code: " + loginFailEvent.getFailureCode());
				setErrorMessage(ErrorCodeMapper.getI18nMessage(loginFailEvent.getFailureCode()));
			}
		});
	}

	public void show() {
		hideMessages();
		modal.show();
	}

	public void hide() {
		hideMessages();
		modal.hide();
	}

	/**
	 * get the password string
	 *
	 * @param event Standard GWT ClickEvent
	 */
	@UiHandler("password")
	public void onUserFocused(FocusEvent event) {
		password.setText("");
	}

	/**
	 * get the email string
	 *
	 * @param event Standard GWT ClickEvent
	 */
	@UiHandler("email")
	public void onEmailFocused(FocusEvent event) {
		email.setText("");
	}

	/**
	 * Handler for form submit
	 *
	 * @param event Standard GWT ClickEvent
	 *              welcomeViewImpl.onSubmit(eMail, psswd ) submits bean for sign-in in
	 *              com.therdl.server.restapi.SessionServlet class
	 */
	@UiHandler("submit")
	public void onSubmit(ClickEvent event) {
		onSubmit();
	}

	@UiHandler("password")
	public void onPassEnter(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			onSubmit();
		}
	}

	@UiHandler("email")
	public void onEmailEnter(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			onSubmit();
		}
	}

	private void onSubmit() {
		Log.info("SignInViewImpl onSubmit");

		String eMail = email.getText();
		String password = this.password.getText();
		Boolean rememberMe = this.rememberMe.getValue();

		Log.info("SignInViewImpl onSubmit eMail password " + eMail + " : " + password);

		if (eMail != null && !eMail.equals("Email") && password != null && !this.password.equals("password")) {
			appMenu.onSubmit(eMail, password, rememberMe);
		}
	}

	/**
	 * click handler for sign up link, redirects to sign up view
	 *
	 * @param event
	 */
	@UiHandler("signUpLink")
	public void onSignUpLinkClicked(ClickEvent event) {
		modal.hide();
		History.newItem(RDLConstants.Tokens.SIGN_UP);
	}

	/**
	 * click handler for sign up link, redirects to sign up view
	 *
	 * @param event
	 */
	@UiHandler("forgotPassLink")
	public void onForgotPassLinkClicked(ClickEvent event) {
		modal.hide();
		appMenu.showForgotPasswordPopUp();
	}

	public String getPassword() {
		return password.getText();
	}

	public org.gwtbootstrap3.client.ui.TextBox getEmail() {
		return email;
	}

	@Override
	public AppMenu getAppMenu() {
		return appMenu;
	}

}
