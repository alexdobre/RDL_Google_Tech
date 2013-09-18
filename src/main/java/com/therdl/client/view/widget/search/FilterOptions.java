package com.therdl.client.view.widget.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.therdl.client.view.widget.SnipSearchWidget;

import java.util.logging.Logger;

/**
 *  Pop-up with filter search options
 *
 */
public class FilterOptions extends PopupPanel  {


    private static Logger log = Logger.getLogger("");

    private static FilterOptionslUiBinder uiBinder = GWT.create(FilterOptionslUiBinder.class);

    interface FilterOptionslUiBinder extends UiBinder<Widget, FilterOptions> {
    }

    private SnipSearchWidget snipSearchWidget;

    @UiField
    TextBox paging;

    @UiField
    Button submit;


    public FilterOptions( SnipSearchWidget view) {
        super(true);
        add(uiBinder.createAndBindUi(this));
        snipSearchWidget =view;
        this.setStyleName("filterOptions");

    }


    @UiHandler("submit")
    public void onSubmit(ClickEvent event) {


        String pageSize = paging.getText();
        snipSearchWidget.triggerSearch(pageSize);
        log.info("FilterOptions onSubmit");
         this.hide();
    }



}
