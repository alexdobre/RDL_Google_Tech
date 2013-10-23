package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.therdl.shared.RDLConstants;


public class BookmarkSearchPopup extends PopupPanel {
    interface BookmarkSearchPopupUiBinder extends UiBinder<HTMLPanel, BookmarkSearchPopup> {
    }

    private static BookmarkSearchPopupUiBinder ourUiBinder = GWT.create(BookmarkSearchPopupUiBinder.class);

    @UiField
    TextBox getLinkTextBox;
    SearchFilterWidget searchFilterWidget;

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

    public StringBuilder formURl() {
        StringBuilder stringBuilder = new StringBuilder();

        if(!searchFilterWidget.title.getText().equals("")) {
            stringBuilder.append("title="+searchFilterWidget.title.getText()+":");
        }

        if(searchFilterWidget.categoryList.getSelectedIndex() != 0) {
            stringBuilder.append("coreCat="+searchFilterWidget.categoryList.getItemText(searchFilterWidget.categoryList.getSelectedIndex())+":");
        }

        if(searchFilterWidget.subCategoryList.getSelectedIndex() != 0) {
            stringBuilder.append("subCat="+searchFilterWidget.subCategoryList.getItemText(searchFilterWidget.subCategoryList.getSelectedIndex())+":");
        }

        if(!searchFilterWidget.posRef.getText().equals("")) {
            stringBuilder.append("posRef="+searchFilterWidget.posRef.getText()+":");
        }

        if(!searchFilterWidget.neutralRef.getText().equals("")) {
            stringBuilder.append("neutralRef="+searchFilterWidget.neutralRef.getText()+":");
        }

        if(!searchFilterWidget.negativeRef.getText().equals("")) {
            stringBuilder.append("negativeRef="+searchFilterWidget.negativeRef.getText()+":");
        }

        if(!searchFilterWidget.snipRep.getText().equals("")) {
            stringBuilder.append("rep="+searchFilterWidget.snipRep.getText()+":");
        }

        if(!searchFilterWidget.content.getText().equals("")) {
            stringBuilder.append("content="+searchFilterWidget.content.getText()+":");
        }

        if(!searchFilterWidget.author.getText().equals("")) {
            stringBuilder.append("author="+searchFilterWidget.author.getText()+":");
        }

        if(!searchFilterWidget.dateFrom.getText().equals("")) {
            stringBuilder.append("dateFrom="+searchFilterWidget.dateFrom.getText()+":");
        }

        if(!searchFilterWidget.dateTo.getText().equals("")) {
            stringBuilder.append("dateTo="+searchFilterWidget.dateTo.getText()+":");
        }


        if(stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder;
    }

}