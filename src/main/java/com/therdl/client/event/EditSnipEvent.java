package com.therdl.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.therdl.client.dto.SnipProxy;

/**
 * This event signals to the application that a snip is to be edited
 * or created. Initially triggered by the menu presenter and handled by 
 * the edit widget
 * @author Alex
 *
 */
public class EditSnipEvent extends GwtEvent<EditSnipEvent.Handler> {
	public static final Type<Handler> TYPE = new Type<Handler>();

	/**
	 * Handles {@link EditSnipEvent}.
	 */
	public interface Handler extends EventHandler {
		void startEdit(SnipProxy snipPorxy, RequestContext requestContext);
	}

	private final SnipProxy snipProxy;
	private final RequestContext request;

	public EditSnipEvent(SnipProxy snipProxy) {
		this(snipProxy, null);
	}

	public EditSnipEvent(SnipProxy snipProxy, RequestContext request) {
		this.snipProxy = snipProxy;
		this.request = request;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.startEdit(snipProxy, request);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

}
