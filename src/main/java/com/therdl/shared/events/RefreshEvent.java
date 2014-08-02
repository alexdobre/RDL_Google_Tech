package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * this is a refresh event to be used whenever a user initaites and action that requires the session to be refreshed
 * for example after uploading a file and the results need to be updated through out all the views
 * or a change password  ect .....
 */
public class RefreshEvent extends GwtEvent<RefreshEventHandler> {

	public static Type<RefreshEventHandler> TYPE = new Type<RefreshEventHandler>();


	@Override
	public Type<RefreshEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RefreshEventHandler refreshEventHandle) {
		refreshEventHandle.onRefreshEvent(this);
	}
}
