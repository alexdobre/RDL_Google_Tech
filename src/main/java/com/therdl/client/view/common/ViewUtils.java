package com.therdl.client.view.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import  com.google.gwt.user.client.ui.PopupPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

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

    /**
     * parses the token and creates searchOptionsBean bean object for search options
     * @return searchOptionsBean
     */
    public static AutoBean<SnipBean> parseToken(Beanery beanery, String token) {
        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
        String[] tokenSplit = token.split(":");
        for (int i = 1; i < tokenSplit.length; i++) {
            String[] keyVal = tokenSplit[i].split("=");
            if (keyVal[0].equals(RDLConstants.BookmarkSearch.TITLE)) {
                searchOptionsBean.as().setTitle(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.CORE_CAT)) {
                searchOptionsBean.as().setCoreCat(keyVal[1].replace("+", " "));
            }

//            if (keyVal[0].equals(RDLConstants.BookmarkSearch.SUB_CAT)) {
//                searchOptionsBean.as().setSubCat(keyVal[1].replace("+", " "));
//            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.POS_REF)) {
                searchOptionsBean.as().setPosRef(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.NEUTRAL_REF)) {
                searchOptionsBean.as().setNeutralRef(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.POSTS)) {
                searchOptionsBean.as().setPosts(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.REP)) {
                searchOptionsBean.as().setRep(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.CONTENT)) {
                searchOptionsBean.as().setContent(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.AUTHOR)) {
                searchOptionsBean.as().setAuthor(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.DATE_FROM)) {
                searchOptionsBean.as().setDateFrom(keyVal[1]);
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.DATE_TO)) {
                searchOptionsBean.as().setDateTo(keyVal[1]);
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.SORT_FIELD)) {
                searchOptionsBean.as().setSortField(keyVal[1]);
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.SORT_ORDER)) {
                searchOptionsBean.as().setSortOrder(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals(RDLConstants.BookmarkSearch.SNIP_TYPE)) {
                searchOptionsBean.as().setSnipType(keyVal[1]);
            }
        }

        return searchOptionsBean;
    }
}
