package com.therdl.client.view.widget;

import com.google.gwt.user.client.ui.Widget;


/**
 * A singleton widget holder for reusable widgets
 * @author Alex
 *
 */
public class WidgetHolder {
	
	private Widget appMenu;
	private Widget snipSearchWidget;
	private Widget leftMenuTree;
	private Widget snipEditor;
    private SnipListRowWidget snipListRowWidget;

	
	//singleton implementation
	private static WidgetHolder instance;
	
	public static  WidgetHolder getInstance(){
		if (instance == null) return new WidgetHolder();
		return instance;
	}
	
	private WidgetHolder(){
		this.appMenu = buildAppMenu();
	}
	

	
	private Widget buildAppMenu() {
		return new AppMenu().asWidget();
	}

	public Widget getAppMenu() {
		return appMenu;
	}


	public Widget getSnipSearchWidget() {
		if(snipSearchWidget == null) snipSearchWidget = new SnipSearchWidget();
		return snipSearchWidget;
	}




}
