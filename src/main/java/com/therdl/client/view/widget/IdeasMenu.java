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

public class IdeasMenu extends Composite{
    interface IdeasMenuUiBinder extends UiBinder<HTMLPanel, IdeasMenu> {
    }

    private static IdeasMenuUiBinder ourUiBinder = GWT.create(IdeasMenuUiBinder.class);

    @UiField
    FocusPanel snipBtn;

    @UiField
    FocusPanel fastCapBtn;

    @UiField
    FocusPanel materialBtn;

    @UiField
    FocusPanel habitBtn;

    @UiField
    SpanElement hoverDiv;

    public IdeasMenu() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("snipBtn")
    public void onMouseOver(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(Constants.SNIP_TEXT);
    }


    @UiHandler("fastCapBtn")
    public void onMouseOver1(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(Constants.FAST_CAP_TEXT);
    }


    @UiHandler("materialBtn")
    public void onMouseOver2(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(Constants.MATERIAL_TEXT);
    }


    @UiHandler("habitBtn")
    public void onMouseOver3(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(Constants.HABIT_TEXT);
    }
}