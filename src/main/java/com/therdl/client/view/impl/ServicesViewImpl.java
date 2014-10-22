package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ServicesView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.runtized.ServicesFilter;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The services view
 */
public class ServicesViewImpl extends AppMenuView implements ServicesView {

	interface ServicesViewImplUiBinder extends UiBinder<Widget, ServicesViewImpl> {
	}

	private static ServicesViewImplUiBinder uiBinder = GWT.create(ServicesViewImplUiBinder.class);

	@UiField
	FlowPanel serviceSearchFilterPanel, serviceListPanel;

	private ServicesView.Presenter presenter;
	private AutoBean<CurrentUserBean> currentUserBean;
	private ServicesFilter servicesFilter;
	private ListWidget listWidget;

	public ServicesViewImpl(AutoBean<CurrentUserBean> cUserBean, AppMenu appMenu) {
		super(appMenu);
		this.currentUserBean = cUserBean;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public ServicesFilter getServicesFilter() {
		return servicesFilter;
	}

	public void setServicesFilter(ServicesFilter servicesFilter) {
		serviceSearchFilterPanel.clear();
		serviceSearchFilterPanel.add(servicesFilter);
		this.servicesFilter = servicesFilter;
	}

	public ListWidget getListWidget() {
		return listWidget;
	}

	public void setListWidget(ListWidget listWidget) {
		serviceListPanel.clear();
		serviceListPanel.add(listWidget);
		this.listWidget = listWidget;
	}
}
