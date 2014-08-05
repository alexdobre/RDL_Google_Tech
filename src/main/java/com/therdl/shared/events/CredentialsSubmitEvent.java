package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;
import com.therdl.shared.LoginHandler;

/**
 * User submits their credentials
 */
public class CredentialsSubmitEvent extends GwtEvent<CredentialsSubmitEventHandler> {
	public static Type<CredentialsSubmitEventHandler> TYPE = new Type<CredentialsSubmitEventHandler>();

	private String emailTxt;
	private String passwordText;
	private Boolean rememberMe;
	private String sid;
	private final LoginHandler loginHandler;

	public CredentialsSubmitEvent(String emailTxt, String passwordText, Boolean rememberMe, String sid, final LoginHandler loginHandler) {
		this.emailTxt = emailTxt;
		this.passwordText = passwordText;
		this.rememberMe = rememberMe;
		this.sid = sid;
		this.loginHandler = loginHandler;
	}

	@Override
	public Type<CredentialsSubmitEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CredentialsSubmitEventHandler eventHandler) {
		eventHandler.onCredentialsSubmitEvent(this);
	}

	public String getEmailTxt() {
		return emailTxt;
	}

	public String getPasswordText() {
		return passwordText;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public String getSid() {
		return sid;
	}

	public LoginHandler getLoginHandler() {
		return loginHandler;
	}
}
