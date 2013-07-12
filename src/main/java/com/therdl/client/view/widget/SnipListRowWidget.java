package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiField;

public class SnipListRowWidget extends Composite {

	private static SnipListRowWidgetUiBinder uiBinder = GWT
			.create(SnipListRowWidgetUiBinder.class);
	@UiField DockPanel snipListRow;
	@UiField
    FlowPanel snipRowRating;
	@UiField Label rating;
	@UiField Label title;
	@UiField Label author;
	@UiField Label authorRep;
	@UiField Label content;

	interface SnipListRowWidgetUiBinder extends
			UiBinder<Widget, SnipListRowWidget> {
	}

	public SnipListRowWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
