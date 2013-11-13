package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.therdl.shared.Constants;

/**
 * gwt widget class for top bar menu in the ideas view
 * contains icons for stream, tag, thread, post
 * when hover the text is shown for each module
 */
public class StoriesMenu extends Composite{
    interface StoriesMenuUiBinder extends UiBinder<HTMLPanel, StoriesMenu> {
    }

    private static StoriesMenuUiBinder ourUiBinder = GWT.create(StoriesMenuUiBinder.class);

    @UiField
    FocusPanel streamBtn;

    @UiField
    FocusPanel tagBtn;

    @UiField
    FocusPanel threadBtn;

    @UiField
    FocusPanel postBtn;

    @UiField
    SpanElement hoverDiv;

    public StoriesMenu() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("streamBtn")
    public void onMouseOver(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML("Stream description placeholder");
    }


    @UiHandler("tagBtn")
    public void onMouseOver1(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML("TAG description placeholder");
    }


    @UiHandler("threadBtn")
    public void onMouseOver2(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML("THREAD description placeholder");
    }


    @UiHandler("postBtn")
    public void onMouseOver3(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML("POST description placeholder");
    }
}