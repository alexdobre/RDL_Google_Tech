package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.therdl.client.view.ExploreView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import org.gwtbootstrap3.client.ui.Button;

/**
 * @author alex
 */
public class ExploreViewImpl extends AppMenuView implements ExploreView {

	interface ExploreViewImplUiBinder extends UiBinder<Widget, ExploreViewImpl> {
	}

	private static ExploreViewImplUiBinder uiBinder = GWT.create(ExploreViewImplUiBinder.class);

	private ExploreView.Presenter presenter;

	@UiField
	Button welcome, license, snips, services, supporter, stories;

	@Inject
	public ExploreViewImpl(AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));

	}

	@UiHandler("welcome")
	public void onWelcome(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.WELCOME);
	}

	@UiHandler("license")
	public void onLicense(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.LICENSE);
	}

	@UiHandler("snips")
	public void onSnips(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.SNIPS);
	}

	@UiHandler("services")
	public void onServices(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.SERVICES);
	}

	@UiHandler("supporter")
	public void onSupporter(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.SUBSCRIBE);
	}

	@UiHandler("stories")
	public void onStories(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.STORIES);
	}

	@Override
	public void setPresenter(ExploreView.Presenter presenter) {
		this.presenter = presenter;
	}
}
