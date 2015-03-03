package com.therdl.client.social.facebook;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

public class SharePanel extends Composite{
	interface SharePanelUiBinder extends UiBinder<HTMLPanel, SharePanel> {
	}
	
	private static SharePanelUiBinder ourUiBinder = GWT.create(SharePanelUiBinder.class);
	
	AutoBean<SnipBean> userSnip;
	AutoBean<CurrentUserBean> userBean;
	
	@UiField
	Button fbButton;
	
	@UiField
	Button twitterButton;
	
	@UiField
	Button youtubeButton;
//	
//	@UiField
//	Anchor fbShare;
	
	@UiField
	FlowPanel socialPanel;
	
	public SharePanel() {
		Facebook.init("1568442000092974");
		initWidget(ourUiBinder.createAndBindUi(this));
		socialPanel.add(createAnchor(Resources.INSTANCE.facebookImage(), Constants.FACEBOOK_URL));
		socialPanel.add(createAnchor(Resources.INSTANCE.twitterImage(), Constants.TWITTER_URL));
		socialPanel.add(createAnchor(Resources.INSTANCE.youtubeImage(), Constants.YOUTUBE_URL));
		initButtons();
		bind();
	}
	
	private void initButtons() {
		twitterButton.addStyleName("twitter-button");
		fbButton.addStyleName("fb-button");
		youtubeButton.addStyleName("youtube-button");
//		fbButton.getElement().getStyle().setBackgroundImage(Resources.INSTANCE.facebookImage().getURL());
//		fbButton.setText("facebook");
//		fbShare = createAnchor(Resources.INSTANCE.facebookImage(), "#");
	}
	
	private void bind() {
		FocusPanel focusPanel = new FocusPanel(socialPanel);
//		socialPanel.getWidget(0).ad
		focusPanel.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Window.alert(event.getSource().toString());
			}
		} , ClickEvent.getType());
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
		img.getElement().getStyle().setCursor(Style.Cursor.POINTER);
		anchor.getElement().appendChild(img.getElement());
		return anchor;
	}
	
	public void setCurrentUserBean(AutoBean<CurrentUserBean> currentUserBean) {
		this.userBean = currentUserBean;
	}
	
	public void setSnipBean(AutoBean<SnipBean> snipBean) {
		this.userSnip = snipBean;
	}
	
	@UiHandler("fbButton")
	public void shareContent(ClickEvent event) {
		Facebook.shareUrl();
	}

}
