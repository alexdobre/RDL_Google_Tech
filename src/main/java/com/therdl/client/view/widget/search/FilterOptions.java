package com.therdl.client.view.widget.search;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.client.view.widget.SnipSearchWidget;
import com.therdl.shared.Constants;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SearchOptionsBean;

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

    @UiField
    TextBox content;

    @UiField
    TextBox author;

    @UiField
    TextBox dateFrom;

    @UiField
    TextBox dateTo;

    @UiField
    HTMLPanel container;

    @UiField
    ListBox categoryList;

    DatePicker datePicker;

    private Beanery beanery = GWT.create(Beanery.class);

    public FilterOptions( SnipSearchWidget view) {
        super(true);
        add(uiBinder.createAndBindUi(this));
        snipSearchWidget =view;
        this.setStyleName("filterOptions");
        createCategoryList();
    }

    void createCategoryList() {
        categoryList.addItem("Select a category");
        for(CoreCategory item : CoreCategory.values())
            categoryList.addItem(item.getShortName());
    }

    @UiHandler("submit")
    public void onSubmit(ClickEvent event) {

        AutoBean<SearchOptionsBean> searchOptionsBean = beanery.searchOptionsBean();

        String pageSize = paging.getText();
        if(!pageSize.equals("")) {
            searchOptionsBean.as().setPageSize(Integer.parseInt(pageSize));
        } else {
            searchOptionsBean.as().setPageSize(Constants.DEFAULT_PAGE_SIZE);
        }

        boolean isOptionsSet = false;

        String contentText = content.getText();
        if(!contentText.equals("")) {
            searchOptionsBean.as().setContent(contentText);
            isOptionsSet = true;
        }

        String authorText = author.getText();
        if(!authorText.equals("")) {
            searchOptionsBean.as().setAuthor(authorText);
            isOptionsSet = true;
        }

        String dateFromText = dateFrom.getText();
        if(!dateFromText.equals("")) {
            searchOptionsBean.as().setDateFrom(dateFromText);
            isOptionsSet = true;
        }

        String dateToText = dateTo.getText();
        if(!dateToText.equals("")) {
            searchOptionsBean.as().setDateTo(dateToText);
            isOptionsSet = true;
        }

        int catIndex = categoryList.getSelectedIndex();
        if(catIndex != 0) {
            searchOptionsBean.as().setCoreCat(categoryList.getItemText(catIndex));
            isOptionsSet = true;
        }

        if(isOptionsSet) {
            snipSearchWidget.triggerSearch(searchOptionsBean);
            log.info("FilterOptions onSubmit");
        }
        this.hide();
    }

    @UiHandler("dateFrom")
    public void onDateFromClicked(ClickEvent event) {
        createDatePicker(dateFrom);
    }

    @UiHandler("dateTo")
    public void onDateToClicked(ClickEvent event) {
        createDatePicker(dateTo);
    }

    public void createDatePicker(final TextBox dateField) {
        final PopupPanel popupPanel=new PopupPanel(true);
        datePicker = new DatePicker();

        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date date = event.getValue();
                String dateString =
                        DateTimeFormat.getFormat("MM/dd/yyyy").format(date);
                dateField.setText(dateString);

                popupPanel.hide();
            }
        });

        int x = dateField.getAbsoluteLeft();
        int y = dateField.getAbsoluteTop();
        popupPanel.setPopupPosition(x, y+20);
        popupPanel.setStyleName("datePicker");
        // Set the default value
        datePicker.setValue(new Date(), true);
        popupPanel.setWidget(datePicker);
        popupPanel.show();
    }

}
