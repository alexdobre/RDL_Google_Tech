package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;
import com.therdl.shared.beans.SnipBean;

/**
 * Show abuse comments
 */
public class ShowAbuseCommentsEvent extends GwtEvent<ShowAbuseCommentsEventHandler> {

	public static GwtEvent.Type<ShowAbuseCommentsEventHandler> TYPE = new GwtEvent.Type<ShowAbuseCommentsEventHandler>();

	private SnipBean abusiveContent;

	public ShowAbuseCommentsEvent(SnipBean abusiveContent) {
		this.abusiveContent = abusiveContent;
	}

	@Override
	public GwtEvent.Type<ShowAbuseCommentsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowAbuseCommentsEventHandler showAbuseCommentsEventHandler) {
		showAbuseCommentsEventHandler.onCommentsEvent(this);
	}

	public SnipBean getAbusiveContent() {
		return abusiveContent;
	}
}