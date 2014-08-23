package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * User has failed login
 */
public interface LoginFailEventHandler extends EventHandler {

	public void onLogFailEvent(LoginFailEvent loginFailEvent);
}
