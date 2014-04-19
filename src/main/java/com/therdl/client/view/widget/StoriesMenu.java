package com.therdl.client.view.widget;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.therdl.client.RDL;
import com.therdl.client.view.common.ViewUtils;

/**
 * gwt widget class for top bar menu in the stories view
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

    @UiField
    org.gwtbootstrap3.client.ui.Button storiesVideoButton;

    public StoriesMenu() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("streamBtn")
    public void onMouseOver(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(RDL.i18n.streamDescription());
    }


    @UiHandler("tagBtn")
    public void onMouseOver1(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(RDL.i18n.tagDescription());
    }


    @UiHandler("threadBtn")
    public void onMouseOver2(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(RDL.i18n.threadDescription());
    }


    @UiHandler("postBtn")
    public void onMouseOver3(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(RDL.i18n.postDescription());
    }

    /**
     * Loads the stories video
     */
    @UiHandler("storiesVideoButton")
    public void onClickVideoIntro(ClickEvent event) {
        //we initialize the welcome video player
        AbstractMediaPlayer player = null;
        try {
            // create the player, specifying URL of media
            player = new YouTubePlayer("O4RRX70shsM","100%", "350px");
            DialogBox dialog = ViewUtils.constructDialogBox(player, event, 450);
            dialog.show();
        } catch(PluginVersionException e) {
            ViewUtils.constructPopup(new HTML(".. please download the necessary plugin.."),event,450).show();
        } catch(PluginNotFoundException e) {
            // catch PluginNotFoundException and display a friendly notice.
            ViewUtils.constructPopup(new HTML(".. plugin not found, please download the necessary plugin to run YouTube .."),event,450).show();
        }
    }
}