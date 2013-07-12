package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.therdl.shared.RDLConstants;

public class SnipSearchWidget extends Composite {

	private static SnipSearchWidgetUiBinder uiBinder = GWT
			.create(SnipSearchWidgetUiBinder.class);
	@UiField Label textSearch;
	@UiField Button searchButton;
	@UiField Button createNewButton;
	@UiField TextBox textBox;
	//@UiField ListBox coreCategoryCombo;

	interface SnipSearchWidgetUiBinder extends
			UiBinder<Widget, SnipSearchWidget> {
	}

	public SnipSearchWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("searchButton")
	void onSearchButtonClick(ClickEvent event) {
		//TODO what to do on search button click
	}
	@UiHandler("createNewButton")
	void onCreateNewButtonClick(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.SNIP_EDIT);
	}
	@UiHandler("textBox")
	void onTextBoxKeyUp(KeyUpEvent event) {
		//TODO IF ENTER then search
	}
}
