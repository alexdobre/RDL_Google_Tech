package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Report abuse
 */
public class ReportAbuseEvent extends GwtEvent<ReportAbuseEventHandler> {

	public static GwtEvent.Type<ReportAbuseEventHandler> TYPE = new GwtEvent.Type<ReportAbuseEventHandler>();

	private String contentId;

	public ReportAbuseEvent(String contentId) {
		this.contentId = contentId;
	}

	@Override
	public GwtEvent.Type<ReportAbuseEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ReportAbuseEventHandler reportAbuseEventHandler) {
		reportAbuseEventHandler.onAbuseEvent(this);
	}

	public String getContentId() {
		return contentId;
	}
}
