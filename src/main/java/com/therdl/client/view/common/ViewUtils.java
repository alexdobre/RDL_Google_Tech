package com.therdl.client.view.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import  com.google.gwt.user.client.ui.PopupPanel;

/**
 * Common methods used by views
 */
public class ViewUtils {

    /**
     * Constructs a popup at the place of the event
     * @param widget
     * @param event
     * @param width
     * @return
     */
    public static DecoratedPopupPanel constructPopup(Widget widget,ClickEvent event, int width){
        final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
        simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
        simplePopup.setAnimationEnabled(false);
        simplePopup.setWidth(""+width+"px");
        simplePopup.setWidget(widget);
        positionRelative(event, simplePopup);
        return simplePopup;
    }

    /**
     * Constructs a dialog box at the place of the event
     * @param widget
     * @param event
     * @param width
     * @return
     */
    public static DialogBox constructDialogBox (Widget widget,ClickEvent event, int width){
        DialogBox dialog = new DialogBox(true,false);
        dialog.ensureDebugId("cwBasicPopup-simplePopup");
        dialog.setAnimationEnabled(true);
        dialog.setWidth(""+width+"px");
        dialog.setWidget(widget);
        positionRelative(event, dialog);
        return dialog;
    }

    private static void positionRelative(ClickEvent event, PopupPanel dialog) {
        // Reposition the popup relative to the button
        Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft() + 10;
        int top = source.getAbsoluteTop() + 10;
        dialog.setPopupPosition(left, top);
    }
}
