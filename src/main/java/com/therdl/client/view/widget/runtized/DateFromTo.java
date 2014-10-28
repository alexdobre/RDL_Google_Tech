package com.therdl.client.view.widget.runtized;

import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Two date time pickers side by side
 */
public class DateFromTo extends Composite {
	interface DateFromToWidgetUiBinder extends UiBinder<Widget, DateFromTo> {
	}

	private static DateFromToWidgetUiBinder uiBinder = GWT.create(DateFromToWidgetUiBinder.class);

	@UiField
	DateTimePicker dateFrom, dateTo;

	public DateFromTo() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DateTimePicker getDateFrom() {
		return dateFrom;
	}

	public DateTimePicker getDateTo() {
		return dateTo;
	}
}
