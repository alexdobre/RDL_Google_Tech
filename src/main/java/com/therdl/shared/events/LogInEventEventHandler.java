package com.therdl.shared.events;


import com.google.gwt.event.shared.EventHandler;

/**
 * Simple event handler for the  log-in event handler no payload required
 */

public interface LogInEventEventHandler  extends EventHandler {

    void onLogInEvent(LogInEvent onLogInEvent);

}
