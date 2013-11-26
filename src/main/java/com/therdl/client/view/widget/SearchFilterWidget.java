package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SubCategory;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * GWT widget class for search filter
 * creates GUI elements and handlers for them
 */

public class SearchFilterWidget extends Composite {

    private static Logger log = Logger.getLogger("");

    private static SnipSearchWidgetUiBinder uiBinder = GWT.create(SnipSearchWidgetUiBinder.class);

    @UiField
    Button submit;

    @UiField
    Button getLinkBtn;

    @UiField
    TextBox title;

    @UiField
    TextBox content;

    @UiField
    TextBox author;

    @UiField
    TextBox dateFrom;

    @UiField
    TextBox dateTo;

    @UiField
    TextBox posRef;

    @UiField
    TextBox neutralRef;

    @UiField
    TextBox negativeRef;

    @UiField
    TextBox snipRep;

    @UiField
    HTMLPanel container;

    @UiField
    ListBox categoryList;

    @UiField
    ListBox subCategoryList;

    DatePicker datePicker;

    SnipSearchView view;

    BookmarkSearchPopup bookmarkSearchPopup;

    private Beanery beanery = GWT.create(Beanery.class);

	interface SnipSearchWidgetUiBinder extends UiBinder<Widget, SearchFilterWidget> { }

    public SearchFilterWidget(SnipSearchViewImpl snipSearchView) {
        initWidget(uiBinder.createAndBindUi(this));
        this.view = snipSearchView;
        createCategoryList();

	}

    /**
     * creates category and subcategory list for snips, when user choose a category subcategory list is refreshed
     */
    void createCategoryList() {
        categoryList.addItem("Select a category");
        for(CoreCategory item : CoreCategory.values()) {
            categoryList.addItem(item.getShortName());
        }

        categoryList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                int selectedIndex = categoryList.getSelectedIndex();
                subCategoryList.clear();
                subCategoryList.addItem("Select a subcategory");
                subCategoryList.setEnabled(false);

                if(selectedIndex != 0) {
                    EnumSet subCategories = CoreCategory.values()[selectedIndex-1].getSubCategories();

                    if(subCategories != null) {
                        for(Iterator it = subCategories.iterator();it.hasNext();){
                            subCategoryList.addItem(((SubCategory)it.next()).getName());
                        }
                        subCategoryList.setEnabled(true);
                    }
                }
            }
        });

        subCategoryList.addItem("Select a subcategory");
        subCategoryList.setEnabled(false);
    }

    /**
     * click handle for link button for bookmark search
     * bookmark search popup is opened in the button side
     * @param event
     */

    @UiHandler("getLinkBtn")
    public void onLinkBtnClicked(ClickEvent event) {
        BookmarkSearchPopup bookmarkSearchPopup = new BookmarkSearchPopup(this);
        int x = getLinkBtn.getAbsoluteLeft();
        int y = getLinkBtn.getAbsoluteTop();
        bookmarkSearchPopup.setPopupPosition(x+getLinkBtn.getOffsetWidth(), y);
        bookmarkSearchPopup.show();
    }

    /**
     * construct snip bean object from the popup fields
     * at the end when there is no any search option gets initial list, else do the filtered search
     * @param event
     */

    @UiHandler("submit")
    public void onSubmit(ClickEvent event) {

        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();

        boolean isOptionsSet = false;

        String titleText = title.getText();
        if(!titleText.equals("")) {
            searchOptionsBean.as().setTitle(titleText);
            isOptionsSet = true;
        }

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

        int subCatIndex = subCategoryList.getSelectedIndex();
        if(subCatIndex != 0) {
            searchOptionsBean.as().setSubCat(subCategoryList.getItemText(subCatIndex));
            isOptionsSet = true;
        }

        String posRefText = posRef.getText();
        if(!posRefText.equals("")) {
            searchOptionsBean.as().setPosRef(Integer.parseInt(posRefText));
            isOptionsSet = true;
        }

        String neutralRefText = neutralRef.getText();
        if(!neutralRefText.equals("")) {
            searchOptionsBean.as().setNeutralRef(Integer.parseInt(neutralRefText));
            isOptionsSet = true;
        }

        String negativeRefText = negativeRef.getText();
        if(!negativeRefText.equals("")) {
            searchOptionsBean.as().setNegativeRef(Integer.parseInt(negativeRefText));
            isOptionsSet = true;
        }

        String snipRepText = snipRep.getText();
        if(!snipRepText.equals("")) {
            searchOptionsBean.as().setRep(Integer.parseInt(snipRepText));
            isOptionsSet = true;
        }

        if(isOptionsSet) {
            log.info("doFilterSearch");
            view.doFilterSearch(searchOptionsBean);
        } else {
            view.getInitialSnipList();
        }
    }

    /**
     * handler for the dateFrom TextBox
     * opens date picker in a popup
     * @param event
     */
    @UiHandler("dateFrom")
    public void onDateFromClicked(ClickEvent event) {
        createDatePicker(dateFrom);
    }

    /**
     * handler for the dateTo TextBox
     * opens date picker in a popup
     * @param event
     */
    @UiHandler("dateTo")
    public void onDateToClicked(ClickEvent event) {
        createDatePicker(dateTo);
    }

    /**
     * creates gwt date picker in a popup
     * sets selected date to the text box
     * @param dateField date TextBox for dateTo or dateFrom
     */

    public void createDatePicker(final TextBox dateField) {
        final PopupPanel popupPanel=new PopupPanel(true);
        datePicker = new DatePicker();

        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date date = event.getValue();
                String dateString =
                        DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
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

    /**
     * handler for the create new button
     * opens create/edit snip view
     * @param event
     */
	@UiHandler("createNewButton")
	void onCreateNewButtonClick(ClickEvent event) {
        History.newItem(RDLConstants.Tokens.SNIP_EDIT);
	}

}
