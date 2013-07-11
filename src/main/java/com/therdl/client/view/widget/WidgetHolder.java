package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.dto.SnipProxy;
import com.therdl.client.view.widget.SnipEditorWorkflow.Driver;

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

	public Widget getLeftMenuTree() {
		if(leftMenuTree == null) leftMenuTree = new LeftMenuTree();
		return leftMenuTree;
	}

	public Widget getSnipEditor() {
		if(snipEditor == null) snipEditor = new SnipEditorWorkflow(null);
		return snipEditor;
	}

}
