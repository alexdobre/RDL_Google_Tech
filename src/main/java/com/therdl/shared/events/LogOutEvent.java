package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Simple  log out event no payload required
 */
public class LogOutEvent  extends GwtEvent<LogOutEventEventHandler> {


    public static Type<LogOutEventEventHandler>  TYPE = new Type<LogOutEventEventHandler>();


    @Override
    public Type<LogOutEventEventHandler> getAssociatedType() {
        return TYPE;
    }



    @Override
    protected void dispatch(LogOutEventEventHandler handler) {
        handler.onLogOutEvent(this);
    }





}
