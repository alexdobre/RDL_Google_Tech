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
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.shared.RDLConstants;

import java.util.logging.Logger;

public class SnipSearchWidget extends Composite {

    private static Logger log = Logger.getLogger("");
    private static SnipSearchWidgetUiBinder uiBinder = GWT
			.create(SnipSearchWidgetUiBinder.class);
	@UiField Label textSearch;
	@UiField Button searchButton;
	@UiField Button createNewButton;
	@UiField TextBox textBox;
    static SnipSearchView view;
    //@UiField ListBox coreCategoryCombo;

	interface SnipSearchWidgetUiBinder extends
			UiBinder<Widget, SnipSearchWidget> {
	}

    /**
     * default constructor
     */
    public SnipSearchWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SnipSearchWidget(SnipSearchViewImpl snipSearchView) {
        initWidget(uiBinder.createAndBindUi(this));
        view = snipSearchView;
	}

	@UiHandler("searchButton")
	void onSearchButtonClick(ClickEvent event) {
		//TODO what to do on search button click
        log.info("SnipSearchWidget Search : onClick " + textBox.getText());
        view.searchSnips(textBox.getText());
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
