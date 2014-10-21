package com.therdl.client.view.widget.runtized;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.shared.beans.SnipBean;

/**
 * The services list
 */
public class ServicesList extends Composite {
	interface ServicesListWidgetUiBinder extends UiBinder<Widget, ServicesList> {
	}

	private static ServicesListWidgetUiBinder uiBinder = GWT.create(ServicesListWidgetUiBinder.class);


	public ServicesList() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void populate(List<SnipBean> servicesList) {
		//TODO implement ServicesList.populate
	}
}