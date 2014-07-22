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
import com.therdl.client.RDL;

/**
 * gwt widget class for top bar menu in the snip list view
 * contains icons for snip, fastcap, material, habit
 * when hover the text is shown for each module
 */
public class IdeasMenu extends Composite {
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

    @UiField
    org.gwtbootstrap3.client.ui.Button ideasVideoButton;

    public IdeasMenu() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("snipBtn")
    public void onMouseOver(MouseOverEvent event) {
        hoverDiv.setInnerHTML(RDL.i18n.snipDescription());
    }


    @UiHandler("fastCapBtn")
    public void onMouseOver1(MouseOverEvent event) {
        hoverDiv.setInnerHTML(RDL.i18n.fastCapDescription());
    }


    @UiHandler("materialBtn")
    public void onMouseOver2(MouseOverEvent event) {
        hoverDiv.setInnerHTML(RDL.i18n.materialDescription());
    }


    @UiHandler("habitBtn")
    public void onMouseOver3(MouseOverEvent event) {
        hoverDiv.setInnerHTML(RDL.i18n.habitDescription());
    }

}