package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * handler for snip view event
 *
 * @onSnipSelectEvent triggers the snip view from a closure JSNI event loop
 */
public interface SnipViewEventHandler extends EventHandler {

	void onSnipSelectEvent(SnipViewEvent event);

}
