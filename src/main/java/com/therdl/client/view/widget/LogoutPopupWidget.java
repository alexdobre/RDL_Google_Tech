package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

public class LogoutPopupWidget extends PopupPanel {
    interface LogoutPopupWidgetUiBinder extends UiBinder<HTMLPanel, LogoutPopupWidget> {
    }

    private static LogoutPopupWidgetUiBinder ourUiBinder = GWT.create(LogoutPopupWidgetUiBinder.class);

    @UiField
    Button okBtn;

    public LogoutPopupWidget() {
        add(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("okBtn")
    public void onOkBtnClicked(ClickEvent event) {
        this.hide();
    }
}