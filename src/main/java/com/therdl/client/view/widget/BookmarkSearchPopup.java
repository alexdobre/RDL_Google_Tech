package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.therdl.shared.RDLConstants;

/**
 * Bookmark search popup, extends gwt PopupPanel panel
 * contains TextBox getLinkTextBox GUI element to show constructed url
 */
public class BookmarkSearchPopup extends PopupPanel {
    interface BookmarkSearchPopupUiBinder extends UiBinder<HTMLPanel, BookmarkSearchPopup> {
    }

    private static BookmarkSearchPopupUiBinder ourUiBinder = GWT.create(BookmarkSearchPopupUiBinder.class);

    @UiField
    TextBox getLinkTextBox;
    SearchFilterWidget searchFilterWidget;

    /**
     * constructor BookmarkSearchPopup
     * forms url and sets to the text box
     * url has the following format example: http://localhost:8080/#snips:title=aaa:coreCat=Compatibility:author=serine
     * @param searchFilterWidget
     */
    public BookmarkSearchPopup(SearchFilterWidget searchFilterWidget) {
        super(true);
        add(ourUiBinder.createAndBindUi(this));
        this.setStyleName("bookmarkSearchPopup");
        setModal(true);

        this.searchFilterWidget = searchFilterWidget;

        String url = "";

        if(formURl().length() != 0){
            url = ":"+formURl().toString().replace(" ","+");
        }

        getLinkTextBox.setText(GWT.getHostPageBaseURL()+"#snips"+ url);
    }

    /**
     * forms bookmark url from the values of the search widget's gui elements
     * @return constructed url as a StringBuilder object
     */
    public StringBuilder formURl() {
        StringBuilder stringBuilder = new StringBuilder();

        if(!searchFilterWidget.title.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.TITLE+"="+searchFilterWidget.title.getText()+":");
        }

        if(searchFilterWidget.categoryList.getSelectedIndex() != 0) {
            stringBuilder.append(RDLConstants.BookmarkSearch.CORE_CAT+"="+searchFilterWidget.categoryList.getItemText(searchFilterWidget.categoryList.getSelectedIndex())+":");
        }

        if(searchFilterWidget.subCategoryList.getSelectedIndex() != 0) {
            stringBuilder.append(RDLConstants.BookmarkSearch.SUB_CAT+"="+searchFilterWidget.subCategoryList.getItemText(searchFilterWidget.subCategoryList.getSelectedIndex())+":");
        }

        if(!searchFilterWidget.posRef.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.POS_REF+"="+searchFilterWidget.posRef.getText()+":");
        }

        if(!searchFilterWidget.neutralRef.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.NEUTRAL_REF+"="+searchFilterWidget.neutralRef.getText()+":");
        }

        if(!searchFilterWidget.negativeRef.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.NEGATIVE_REF+"="+searchFilterWidget.negativeRef.getText()+":");
        }

        if(!searchFilterWidget.snipRep.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.REP+"="+searchFilterWidget.snipRep.getText()+":");
        }

        if(!searchFilterWidget.content.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.CONTENT+"="+searchFilterWidget.content.getText()+":");
        }

        if(!searchFilterWidget.author.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.AUTHOR+"="+searchFilterWidget.author.getText()+":");
        }

        if(!searchFilterWidget.dateFrom.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.DATE_FROM+"="+searchFilterWidget.dateFrom.getText()+":");
        }

        if(!searchFilterWidget.dateTo.getText().equals("")) {
            stringBuilder.append(RDLConstants.BookmarkSearch.DATE_TO+"="+searchFilterWidget.dateTo.getText()+":");
        }

        stringBuilder.append(RDLConstants.BookmarkSearch.SORT_FIELD+"="+searchFilterWidget.getSortField()+":");
        stringBuilder.append(RDLConstants.BookmarkSearch.SORT_ORDER+"="+searchFilterWidget.getSortOrder()+":");

        if(stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder;
    }

}