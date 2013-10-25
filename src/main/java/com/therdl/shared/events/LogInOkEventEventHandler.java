package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Simple event handler for the  log-in OK event  no payload required
 *
 * @ onLogInOkEvent(LogInEvent onLogInEvent)
 * updates the status of the menu for a login event
 */
public interface LogInOkEventEventHandler extends EventHandler {

    void onLogInOkEvent(LogInOkEvent onLogInOkEvent);

}
