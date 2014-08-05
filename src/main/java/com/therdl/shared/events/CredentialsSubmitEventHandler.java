package com.therdl.shared.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * User submits their credentials
 */
public interface CredentialsSubmitEventHandler extends EventHandler {

	void onCredentialsSubmitEvent (CredentialsSubmitEvent event);
}
