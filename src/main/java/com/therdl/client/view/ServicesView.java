package com.therdl.client.view;

import com.therdl.client.view.widget.runtized.ServicesFilter;
import com.therdl.client.view.widget.runtized.ServicesList;

/**
 * You can promote your services on your site
 */
public interface ServicesView extends RdlView{

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);

	public ServicesList getServicesList();

	public void setServicesList(ServicesList servicesList);

	public ServicesFilter getServicesFilter();

	public void setServicesFilter(ServicesFilter servicesFilter);
}
