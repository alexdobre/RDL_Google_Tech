package com.therdl.client.view.widget;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;

import com.gargoylesoftware.htmlunit.javascript.host.Console;
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
import com.google.gwt.user.client.ui.CustomButton.Face;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.reveregroup.gwt.facebook4gwt.FacebookStory;
import com.therdl.client.social.facebook.Facebook;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;

/**
 * gwt widget class for the social media icons (facebook, twitter, youtube and reddit)
 * used in the welcome page and snip list view
 */
public class SocialPanel extends Composite {
	interface SocialPanelUiBinder extends UiBinder<HTMLPanel, SocialPanel> {
	}

	private static SocialPanelUiBinder ourUiBinder = GWT.create(SocialPanelUiBinder.class);

	@UiField
	FlowPanel socialPanel;
		
	
	/**
	 * adds anchor elements for 4 social media icons to the socialPanel
	 */
	public SocialPanel() {
		initWidget(ourUiBinder.createAndBindUi(this));
//		Facebook.init("1568442000092974");
//		fbShare = createAnchor(Resources.INSTANCE.facebookImage(), "");
		socialPanel.add(createAnchor(Resources.INSTANCE.facebookImage(), Constants.FACEBOOK_URL));
		socialPanel.add(createAnchor(Resources.INSTANCE.twitterImage(), Constants.TWITTER_URL));
		socialPanel.add(createAnchor(Resources.INSTANCE.youtubeImage(), Constants.YOUTUBE_URL));
//		bind();
	}
	
	
	/**
	 * creates gwt Anchor element for the given image resource and url
	 *
	 * @param imgRes image resource
	 * @param url    url to link
	 * @return
	 */

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
	
	
}