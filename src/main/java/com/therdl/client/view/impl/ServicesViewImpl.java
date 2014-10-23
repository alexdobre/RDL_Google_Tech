package com.therdl.client.view.impl;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ServicesView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.SnipListRow;
import com.therdl.client.view.widget.runtized.ServicesFilter;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * The services view
 */
public class ServicesViewImpl extends AbstractSnipSearch implements ServicesView {

	interface ServicesViewImplUiBinder extends UiBinder<Widget, ServicesViewImpl> {
	}

	private static ServicesViewImplUiBinder uiBinder = GWT.create(ServicesViewImplUiBinder.class);

	private ServicesView.Presenter presenter;
	private ServicesFilter servicesFilter;

	public ServicesViewImpl(AutoBean<CurrentUserBean> cUserBean, AppMenu appMenu) {
		super(appMenu);
		this.currentUserBean = cUserBean;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
		if (snipList == null) {
			snipList = new ListWidget(currentUserBean, beanList, pageIndex, new SnipListRow(), presenter);
			snipListRowContainer.add(snipList);
		} else {
			snipList.populateList(currentUserBean, beanList);
		}
		this.currentSearchOptionsBean.as().setPageIndex(pageIndex);
		servicesFilter.populate(currentSearchOptionsBean);
		ViewUtils.hide(loadingWidget);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public ServicesFilter getServicesFilter() {
		return servicesFilter;
	}

	@Override
	public void setServicesFilter(ServicesFilter servicesFilter) {
		snipSearchWidgetPanel.clear();
		snipSearchWidgetPanel.add(servicesFilter);
		this.servicesFilter = servicesFilter;
	}

	@Override
	public void doFilterSearch() {
		ViewUtils.show(loadingWidget);
		presenter.searchSnips(currentSearchOptionsBean);
	}

}
