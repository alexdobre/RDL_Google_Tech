package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.ServicesView;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * The services presenter
 */
public class ServicesPresenter extends RdlAbstractPresenter<ServicesView> implements ServicesView.Presenter, ServiceFilterRunt {

	private AutoBean<CurrentUserBean> currentUserBean;

	private WidgetHolder holder;

	public ServicesPresenter(ServicesView servicesView, AppController controller) {
		super(controller);
		this.currentUserBean = controller.getCurrentUserBean();
		this.view = servicesView;
		this.view.setPresenter(this);
		this.holder = WidgetHolder.getHolder();
	}

	@Override
	public void go(HasWidgets container, final AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		this.currentUserBean = currentUserBean;
		container.clear();
		if (view.getServicesFilter() == null) {
			//init the services filter
			view.setServicesFilter(holder.getServicesFilter(this));
		}
		if (view.getServicesFilter().getSearchOptions() == null) {
			//init the search options on the filter
			view.getServicesFilter().populate(initSearchOptions());
		}

		//TODO grab the services from the DB

		container.add(view.asWidget());
		view.getAppMenu().setActive();
	}

	private AutoBean<SnipBean> initSearchOptions() {
		AutoBean<SnipBean> searchOptions = beanery.snipBean();
		searchOptions.as().setPageIndex(0);
		searchOptions.as().setSortOrder(-1);
		searchOptions.as().setSortField("creationDate");
		searchOptions.as().setReturnSnipContent(false);
		searchOptions.as().setSnipType(SnipType.SERVICE.getSnipType());
		return searchOptions;
	}
}
