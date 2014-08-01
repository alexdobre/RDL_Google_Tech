package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

import java.util.Date;

/**
 * DateFilterWidget contains 2 TextBox to select dates (from, to). When clicking to TextBox, opens datePicker, to choose dates.
 * Used in search filter widgets (snip search filter and reference serach filter)
 */
public class DateFilterWidget extends Composite {
	interface DateFilterWidgetUiBinder extends UiBinder<HTMLPanel, DateFilterWidget> {
	}

	private static DateFilterWidgetUiBinder ourUiBinder = GWT.create(DateFilterWidgetUiBinder.class);

	@UiField
	TextBox dateFrom, dateTo;

	DatePicker datePicker;

	public DateFilterWidget() {
		initWidget(ourUiBinder.createAndBindUi(this));

	}

	/**
	 * handler for the dateFrom TextBox
	 * opens date picker in a popup
	 *
	 * @param event
	 */
	@UiHandler("dateFrom")
	public void onDateFromClicked(ClickEvent event) {
		createDatePicker(dateFrom);
	}

	/**
	 * handler for the dateTo TextBox
	 * opens date picker in a popup
	 *
	 * @param event
	 */
	@UiHandler("dateTo")
	public void onDateToClicked(ClickEvent event) {
		createDatePicker(dateTo);
	}

	/**
	 * creates gwt date picker in a popup
	 * sets selected date to the text box
	 *
	 * @param dateField date TextBox for dateTo or dateFrom
	 */

	public void createDatePicker(final TextBox dateField) {
		final PopupPanel popupPanel = new PopupPanel(true);
		datePicker = new DatePicker();

		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				String dateString =
						DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
				dateField.setText(dateString);

				popupPanel.hide();
			}
		});

		int x = dateField.getAbsoluteLeft();
		int y = dateField.getAbsoluteTop();
		popupPanel.setPopupPosition(x, y + 20);
		popupPanel.setStyleName("datePicker");
		// Set the default value
		datePicker.setValue(new Date(), true);
		popupPanel.setWidget(datePicker);
		popupPanel.show();
	}

	/**
	 * returns dateFrom TextBox value
	 *
	 * @return
	 */
	public String getDateFrom() {
		return dateFrom.getValue();
	}

	/**
	 * returns dateTo TextBox value
	 *
	 * @return
	 */
	public String getDateTo() {
		return dateTo.getValue();
	}

	/**
	 * sets dateFrom TextBox value
	 *
	 * @param date
	 */
	public void setDateFrom(String date) {
		dateFrom.setText(date);
	}

	/**
	 * sets dateTo TextBox value
	 *
	 * @param date
	 */
	public void setDateTo(String date) {
		dateTo.setText(date);
	}

}