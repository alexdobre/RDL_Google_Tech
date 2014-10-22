package com.therdl.client.view;

import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.runtized.ServicesFilter;

/**
 * You can promote your services on your site
 */
public interface ServicesView extends RdlView {

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);

	public ServicesFilter getServicesFilter();

	public void setServicesFilter(ServicesFilter servicesFilter);

	public ListWidget getListWidget();

	public void setListWidget(ListWidget listWidget);
}
