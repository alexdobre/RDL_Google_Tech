package com.therdl.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface AuthEventHandler extends EventHandler {
  
  public void onAuthEvent(AuthEvent event);
  
}
