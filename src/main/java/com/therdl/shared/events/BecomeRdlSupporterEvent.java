package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Become an RDL supporter
 */
public class BecomeRdlSupporterEvent extends GwtEvent<BecomeRdlSupporterEventHandler> {

	public static GwtEvent.Type<BecomeRdlSupporterEventHandler> TYPE = new GwtEvent.Type<BecomeRdlSupporterEventHandler>();

	private String popupTitle;

	public BecomeRdlSupporterEvent(String popupTitle) {
		this.popupTitle = popupTitle;
	}

	@Override
	public GwtEvent.Type<BecomeRdlSupporterEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(BecomeRdlSupporterEventHandler becomeRdlSupporterEventHandler) {
		becomeRdlSupporterEventHandler.onSupporterEvent(this);
	}

	public String getPopupTitle() {
		return popupTitle;
	}
}
