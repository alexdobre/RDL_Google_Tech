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
import com.therdl.client.view.widget.search.FilterOptions;
import com.therdl.shared.RDLConstants;

import java.util.logging.Logger;

public class SnipSearchWidget extends Composite {

    private static Logger log = Logger.getLogger("");

    private static SnipSearchWidgetUiBinder uiBinder = GWT.create(SnipSearchWidgetUiBinder.class);

    FilterOptions filterOptions;

	@UiField Button searchButton;
	@UiField Button createNewButton;
    @UiField Button optionsButton;
	@UiField TextBox matchText;

    SnipSearchView view;

	interface SnipSearchWidgetUiBinder extends UiBinder<Widget, SnipSearchWidget> { }

    public SnipSearchWidget(SnipSearchViewImpl snipSearchView) {
        initWidget(uiBinder.createAndBindUi(this));
        this.view = snipSearchView;
        this.setStyleName("snipSearchPanel");
	}


    @UiHandler("optionsButton")
    void selectSearchOptions(ClickEvent event) {

        log.info("SnipSearchWidget optionsButton " );

        filterOptions = new FilterOptions(this);
        filterOptions.setGlassEnabled(true);
        filterOptions.setModal(true);
        filterOptions.setPopupPosition(0,10);
        filterOptions.show();


    }


	@UiHandler("searchButton")
	void onSearchButtonClick(ClickEvent event) {

        log.info("SnipSearchWidget Search : onClick " + matchText.getText());
        if(view == null)   log.info("SnipSearchWidget Search : onClick view is null "  );
        view.searchSnips(matchText.getText());
    }


	@UiHandler("createNewButton")
	void onCreateNewButtonClick(ClickEvent event) {
		History.newItem(RDLConstants.Tokens.SNIP_EDIT);
	}


    public void triggerSearch(String pageSize) {

        view.doFilterSearch(pageSize);
    }

}
