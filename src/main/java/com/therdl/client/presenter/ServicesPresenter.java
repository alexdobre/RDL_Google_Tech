package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.app.FuncFactory;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.ServicesView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The services presenter
 */
public class ServicesPresenter extends RdlAbstractPresenter<ServicesView> implements ServicesView.Presenter, ServiceFilterRunt {

	private AutoBean<CurrentUserBean> currentUserBean;
	private GrabSnipFunc grabSnipFunc;
	private WidgetHolder holder;

	public ServicesPresenter(ServicesView servicesView, AppController controller) {
		super(controller);
		this.view = servicesView;
		this.view.setPresenter(this);
		this.grabSnipFunc = FuncFactory.createGrabSnipFunc();
		this.holder = WidgetHolder.getHolder();
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		this.currentUserBean = currentUserBean;
		container.clear();
		view.setServicesFilter(holder.getServicesFilter(this));
		view.setServicesList(holder.getServicesList());
		//TODO grab services from the DB

		container.add(view.asWidget());
		view.getAppMenu().setActive();
	}
}
