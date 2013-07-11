package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.widget.WidgetHolder;

public class SnipSearchViewImpl<T> extends Composite implements SnipSearchView<T> {

	private static SnipSearchViewImplUiBinder uiBinder = GWT
			.create(SnipSearchViewImplUiBinder.class);

    @Override
    public void setPresenter(Presenter<T> presenter) {
        this.presenter = presenter;
    }

    interface SnipSearchViewImplUiBinder extends
			UiBinder<Widget, SnipSearchViewImpl> {
	}
	
	private Presenter<T> presenter;
	
	@UiField Widget appMenu;
	@UiField Widget snipSearchWidget;
	@UiField Widget leftMenuTree;

	public SnipSearchViewImpl(EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));
		appMenu =  WidgetHolder.getInstance().getAppMenu();
		snipSearchWidget = WidgetHolder.getInstance().getSnipSearchWidget();
		leftMenuTree = WidgetHolder.getInstance().getLeftMenuTree();
	}


}
