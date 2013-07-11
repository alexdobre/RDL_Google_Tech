package com.therdl.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.therdl.client.dto.SnipProxy;

/**
 * This event signals to the application that a snip is to be viewed
 * or created. Can be triggered by the list and handled by
 * the view widget
 * @author Alex
 *
 */
public class ViewSnipEvent extends GwtEvent<ViewSnipEvent.Handler> {
	public static final Type<Handler> TYPE = new Type<Handler>();

	/**
	 * Handles {@link ViewSnipEvent}.
	 */
	public interface Handler extends EventHandler {
		void startView(SnipProxy snipPorxy, RequestContext requestContext);
	}

	private final SnipProxy snipProxy;
	private final RequestContext request;

	public ViewSnipEvent(SnipProxy snipProxy) {
		this(snipProxy, null);
	}

	public ViewSnipEvent(SnipProxy snipProxy, RequestContext request) {
		this.snipProxy = snipProxy;
		this.request = request;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.startView(snipProxy, request);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

}
