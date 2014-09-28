package com.therdl.client.view.impl;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.LoadingWidget;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Common functionality for snip search
 */
public abstract class AbstractSnipSearch extends AppMenuView{

	@UiField
	protected FlowPanel snipSearchWidgetPanel;

	@UiField
	protected FlowPanel snipListRowContainer;
	protected ListWidget snipList;

	@UiField
	protected LoadingWidget loadingWidget;

	protected AutoBean<CurrentUserBean> currentUserBean;

	protected AutoBean<SnipBean> currentSearchOptionsBean;

	public AbstractSnipSearch(AppMenu appMenu) {
		super(appMenu);
	}

	public AutoBean<CurrentUserBean> getCurrentUserBean() {
		return currentUserBean;
	}

	public AutoBean<SnipBean> getCurrentSearchOptionsBean() {
		return currentSearchOptionsBean;
	}

	public void setCurrentSearchOptionsBean(AutoBean<SnipBean> snipBean) {
		this.currentSearchOptionsBean = snipBean;
	}

	public ListWidget getListWidget() {
		return snipList;
	}
}
