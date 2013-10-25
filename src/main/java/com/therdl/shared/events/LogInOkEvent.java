package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Simple  log-in ok event no payload required
 * handlers use this event to update the status of the menu
 * for a log in successfully authorisation flow
 */
public class LogInOkEvent extends GwtEvent<LogInOkEventEventHandler> {


    public static Type<LogInOkEventEventHandler> TYPE = new Type<LogInOkEventEventHandler>();

    @Override
    public Type<LogInOkEventEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LogInOkEventEventHandler logInOkEventEventHandler) {
        logInOkEventEventHandler.onLogInOkEvent(this);
    }
}
