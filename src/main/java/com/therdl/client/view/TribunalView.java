package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.presenter.PaginationPresenter;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * The tribunal view
 */
public interface TribunalView extends RdlView {

	public interface Presenter extends PaginationPresenter{
		void searchSnips(AutoBean<SnipBean> searchOptionsBean);

		AppController getController();
	}

	void setPresenter(Presenter presenter);

	Presenter getPresenter();

	AutoBean<CurrentUserBean> getCurrentUserBean();

	AutoBean<SnipBean> getCurrentSearchOptionsBean();

	void setCurrentSearchOptionsBean(AutoBean<SnipBean> snipBean);

	Widget asWidget();

	void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

	ListWidget getListWidget();

	void doFilterSearch();
}
