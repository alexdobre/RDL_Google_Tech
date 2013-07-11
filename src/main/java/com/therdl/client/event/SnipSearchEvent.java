package com.therdl.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.therdl.client.dto.SnipProxy;

/**
 * This event signals to the application that a snip list and search needs to be displayed
 * Initially triggered by the menu presenter or the snip search and handled by 
 * the list snips widget
 * @author Alex
 *
 */

public class SnipSearchEvent extends GwtEvent<SnipSearchEvent.Handler> {
	public static final Type<Handler> TYPE = new Type<Handler>();

	/**
	 * Handles {@link SnipSearchEvent}.
	 */
	public interface Handler extends EventHandler {
		void startList(List<SnipProxy> snipProxyList, RequestContext requestContext);
	}

	private final List<SnipProxy> snipProxyList;
	private final RequestContext request;

	public SnipSearchEvent(List<SnipProxy> snipProxyList) {
		this(snipProxyList, null);
	}

	public SnipSearchEvent(List<SnipProxy> snipProxyList, RequestContext request) {
		this.snipProxyList = snipProxyList;
		this.request = request;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.startList(snipProxyList, request);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

}
