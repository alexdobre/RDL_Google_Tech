package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class LoadingWidget extends Composite {
    interface LoadingWidgetUiBinder extends UiBinder<HTMLPanel, LoadingWidget> {
    }

    private static LoadingWidgetUiBinder ourUiBinder = GWT.create(LoadingWidgetUiBinder.class);

    @UiField
    FlowPanel loadingPanel;

    public LoadingWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setSizes(String width, String height) {
        loadingPanel.getElement().getStyle().setProperty("width", width);
        loadingPanel.getElement().getStyle().setProperty("height", height);

    }
}