package com.therdl.client.view.impl;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.widget.AppMenu;

/**
 * Encapsulates functionality for a view which contains the app menu
 */
public abstract class AppMenuView extends Composite {

	@UiField
	SimplePanel appMenuPanel;
	AppMenu appMenu;

	public AppMenuView(AppMenu appMenu) {
		this.appMenu = appMenu;
	}

	@Override
	public Widget asWidget() {
		this.appMenuPanel.clear();
		this.appMenuPanel.add(appMenu);
		return super.asWidget();
	}

	public AppMenu getAppMenu() {
		return this.appMenu;
	}
}
