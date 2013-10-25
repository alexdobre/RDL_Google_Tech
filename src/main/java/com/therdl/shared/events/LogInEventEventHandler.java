package com.therdl.shared.events;


import com.google.gwt.event.shared.EventHandler;

/**
 * Simple event handler for the  log-in event
 *
 * @ onLogInEvent(LogInEvent onLogInEvent)
 * updates the status of the menu for a login event
 */

public interface LogInEventEventHandler extends EventHandler {

    void onLogInEvent(LogInEvent onLogInEvent);

}
