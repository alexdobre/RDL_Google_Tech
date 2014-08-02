package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Simple event handler for the LogOut event  no payload required
 *
 * @ onLogOutEvent(LogInEvent onLogInEvent)
 * updates the status of the menu for a login event
 */
public interface LogOutEventEventHandler extends EventHandler {


	void onLogOutEvent(LogOutEvent onLogOutEvent);


}
