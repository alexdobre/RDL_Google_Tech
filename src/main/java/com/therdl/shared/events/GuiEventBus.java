package com.therdl.shared.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * the simplest possible client event bus
 * <p/>
 * while its is confusing to a new developer to follow code flow when there are many events
 * it is still a good idea to have all these events as it allows loose coupling of the objects at run time
 *
 * @ EventBus EVENT_BUS see
 * http://www.gwtproject.org/javadoc/latest/com/google/web/bindery/event/shared/SimpleEventBus.html
 */
public class GuiEventBus {


	public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

}
