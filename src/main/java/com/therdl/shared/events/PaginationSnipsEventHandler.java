package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handle user pagination
 */
public interface PaginationSnipsEventHandler extends EventHandler {

	void onPagination (PaginationSnipsEvent event);
}
