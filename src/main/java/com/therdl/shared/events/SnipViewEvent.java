package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * opens a snip in the snip view
 * passes in the author and title for look up
 */
public class SnipViewEvent  extends GwtEvent<SnipViewEventHandler>  {

    public static Type<SnipViewEventHandler>  TYPE = new Type<SnipViewEventHandler>();

    private final String title;
    private final String author;

    public SnipViewEvent(String title, String author) {
        this.title = title;
        this.author = author;
    }

    @Override
    public Type<SnipViewEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SnipViewEventHandler snipViewEventHandler) {

        snipViewEventHandler.onSnipSelectEvent(this);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
