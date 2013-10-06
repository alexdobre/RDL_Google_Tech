package com.therdl.shared.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * the simplest possible client event bus
 *
 * while its is confusing to a new develpor to follow code flow when there are many events
 * it is still a good idea to have events as it allow loose couplinmg of the objects at run time
 *
 */
public class GuiEventBus {


    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

}
