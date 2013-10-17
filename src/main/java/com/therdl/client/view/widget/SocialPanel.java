package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.Constants;

public class SocialPanel extends Composite{
    interface SocialPanelUiBinder extends UiBinder<HTMLPanel, SocialPanel> {
    }

    private static SocialPanelUiBinder ourUiBinder = GWT.create(SocialPanelUiBinder.class);

    @UiField
    FlowPanel socialPanel;

    public SocialPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        socialPanel.add(createAnchor(Resources.INSTANCE.facebookImage(), Constants.FACEBOOK_URL));
        socialPanel.add(createAnchor(Resources.INSTANCE.twitterImage(), Constants.TWITTER_URL));
        socialPanel.add(createAnchor(Resources.INSTANCE.youtubeImage(), Constants.YOUTUBE_URL));
        socialPanel.add(createAnchor(Resources.INSTANCE.redditImage(), Constants.REDDIT_URL));
    }

    public Anchor createAnchor(ImageResource imgRes, final String url) {
        Anchor anchor = new Anchor();
        anchor.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        anchor.setHref(url);
        anchor.setTarget("_blank");
        Image img = new Image(imgRes.getSafeUri().asString());
        img.setWidth("40px");
        img.setHeight("40px");
        img.setStyleName("imgSocial");
        anchor.getElement().appendChild(img.getElement());
        return anchor;
    }
}