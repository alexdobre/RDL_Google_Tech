package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * opens a snip in the snip view
 * passes in the author and title for look up
 *
 * @ String snipId  unique identifier for a snip
 */
public class SnipViewEvent extends GwtEvent<SnipViewEventHandler> {

    public static Type<SnipViewEventHandler> TYPE = new Type<SnipViewEventHandler>();

    private final String snipId;

    public SnipViewEvent(String snipId) {
        this.snipId = snipId;
    }

    @Override
    public Type<SnipViewEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SnipViewEventHandler snipViewEventHandler) {

        snipViewEventHandler.onSnipSelectEvent(this);
    }

    public String getSnipId() {
        return snipId;
    }

}
