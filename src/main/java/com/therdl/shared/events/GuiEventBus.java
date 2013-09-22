package com.therdl.shared.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * the simplest possible client event bus
 */
public class GuiEventBus {


    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

}
