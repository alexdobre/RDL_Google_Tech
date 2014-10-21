package com.therdl.client.app;

import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.widget.runtized.ReplyEditWidget;
import com.therdl.client.view.widget.runtized.ServicesFilter;
import com.therdl.client.view.widget.runtized.ServicesList;

/**
 * Because the AppController is getting a bit bloated we place the long lived widget implementations inside this
 * WidgetHolder class used as a factory class for widgets.
 * Singleton implementation and initialized at the same time as the AppController
 */
public class WidgetHolder {

	private static WidgetHolder holder;
	private AppController controller;

	private ReplyEditWidget replyEditWidget;
	private ServicesFilter servicesFilter;
	private ServicesList servicesList;

	private WidgetHolder(AppController controller) {
		this.controller = controller;
	}

	/**
	 * Initializes the widget holder
	 */
	public static void initHolder(AppController controller) {
		holder = new WidgetHolder(controller);
	}

	public static WidgetHolder getHolder() {
		return holder;
	}


	public ReplyEditWidget getReplyEditWidget(ReplyRunt replyRunt) {
		if (replyEditWidget == null) {
			replyEditWidget = new ReplyEditWidget(replyRunt);
		} else {
			replyEditWidget.setReplyRunt(replyRunt);
		}
		return replyEditWidget;
	}

	public ServicesFilter getServicesFilter(ServiceFilterRunt serviceFilterRunt) {
		if (servicesFilter == null) {
			servicesFilter = new ServicesFilter();
		}
		servicesFilter.setServiceFilterRunt(serviceFilterRunt);
		return servicesFilter;
	}

	public ServicesList getServicesList() {
		if (servicesList == null) {
			servicesList = new ServicesList();
		}
		return servicesList;
	}
}
