package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Report abuse event handler
 */
public interface ReportAbuseEventHandler  extends EventHandler {

	void onAbuseEvent(ReportAbuseEvent event);
}