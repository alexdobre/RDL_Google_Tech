package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 */
public class LogInOkEvent extends GwtEvent<LogInOkEventEventHandler> {


    public static Type<LogInOkEventEventHandler>  TYPE = new Type<LogInOkEventEventHandler>();

    @Override
    public Type<LogInOkEventEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LogInOkEventEventHandler logInOkEventEventHandler) {
        logInOkEventEventHandler.onLogInOkEvent(this);
    }
}
