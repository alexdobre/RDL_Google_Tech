package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Show abuse comments
 */
public interface ShowAbuseCommentsEventHandler extends EventHandler {

	void onCommentsEvent(ShowAbuseCommentsEvent event);

}