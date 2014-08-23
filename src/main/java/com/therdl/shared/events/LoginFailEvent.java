package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * User login has failed
 */
public class LoginFailEvent extends GwtEvent<LoginFailEventHandler> {


	private String failureCode;

	public LoginFailEvent(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public static Type<LoginFailEventHandler> TYPE = new Type<LoginFailEventHandler>();

	@Override
	public Type<LoginFailEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginFailEventHandler loginFailEventHandler) {
		loginFailEventHandler.onLogFailEvent(this);
	}

}
