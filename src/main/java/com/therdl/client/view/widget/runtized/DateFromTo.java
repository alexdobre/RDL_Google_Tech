package com.therdl.client.view.widget.runtized;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Two date time pickers side by side
 */
public class DateFromTo extends Composite {
	interface DateFromToWidgetUiBinder extends UiBinder<Widget, DateFromTo> {
	}

	private static DateFromToWidgetUiBinder uiBinder = GWT.create(DateFromToWidgetUiBinder.class);


	public DateFromTo() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void populate(AutoBean<SnipBean> searchOptions) {
		//TODO implement ServicesList.populate
	}
}
