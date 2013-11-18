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
import com.therdl.shared.Constants;

/**
 * gwt widget class for top bar menu in the improvements view
 * contains icons for stream, tag, thread, post
 * when hover the text is shown for each module
 */
public class ImprovementsMenu extends Composite{
    interface ImprovementsMenuUiBinder extends UiBinder<HTMLPanel, ImprovementsMenu> {
    }

    private static ImprovementsMenuUiBinder ourUiBinder = GWT.create(ImprovementsMenuUiBinder.class);

    @UiField
    FocusPanel proposalBtn;

    @UiField
    FocusPanel pledgeBtn;

    @UiField
    SpanElement hoverDiv;

    public ImprovementsMenu() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("proposalBtn")
    public void onMouseOver(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(RDL.i18n.proposalDescription());
    }


    @UiHandler("pledgeBtn")
    public void onMouseOver1(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(RDL.i18n.pledgeDescription());
    }

}