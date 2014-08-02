package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Simple event handler for the  RefreshEvent Event
 *
 * @ onLogOutEvent(LogInEvent onLogInEvent)
 * updates the status of the menu for a login event
 */
public interface RefreshEventHandler extends EventHandler {

	void onRefreshEvent(RefreshEvent e);
}
