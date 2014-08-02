package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Simple  log-in event no payload required
 * handlers use this event to update the status of the menu
 */
public class LogInEvent extends GwtEvent<LogInEventEventHandler> {

	public static Type<LogInEventEventHandler> TYPE = new Type<LogInEventEventHandler>();

	@Override
	public Type<LogInEventEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogInEventEventHandler logInEventEventHandler) {
		logInEventEventHandler.onLogInEvent(this);
	}
}
