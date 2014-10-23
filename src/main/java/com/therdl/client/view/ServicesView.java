package com.therdl.client.view;

import java.util.ArrayList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.presenter.PaginationPresenter;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.runtized.ServicesFilter;
import com.therdl.shared.beans.SnipBean;

/**
 * You can promote your services on your site
 */
public interface ServicesView extends RdlView {

	public interface Presenter extends PaginationPresenter{
		void searchSnips(AutoBean<SnipBean> searchOptionsBean);
	}

	void setPresenter(Presenter presenter);

	public ServicesFilter getServicesFilter();

	public void setServicesFilter(ServicesFilter servicesFilter);

	void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

	ListWidget getListWidget();

	AutoBean<SnipBean> getCurrentSearchOptionsBean();

	void setCurrentSearchOptionsBean(AutoBean<SnipBean> snipBean);

	void doFilterSearch();

}
