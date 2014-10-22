package com.therdl.client.view.widget.runtized;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.beans.SnipBean;

/**
 * The services search filter
 */
public class ServicesFilter extends Composite {

	private ServiceFilterRunt serviceFilterRunt;

	interface ServicesFilterWidgetUiBinder extends UiBinder<Widget, ServicesFilter> {
	}

	private static ServicesFilterWidgetUiBinder uiBinder = GWT.create(ServicesFilterWidgetUiBinder.class);

	@UiField
	TextBox author, posRef, neutralRef, negativeRef;
	@UiField
	DateFromTo dateFromTo;
	@UiField
	Button filter, createNewButton;
	@UiField
	ListBox categoryList;
	@UiField
	SortBit authorSortBit, dateSortBit;

	private AutoBean<SnipBean> searchOptions;

	public ServicesFilter() {
		initWidget(uiBinder.createAndBindUi(this));
		ViewUtils.createCategoryList(categoryList);
	}

	public void populate(AutoBean<SnipBean> searchOptionsBean) {
		this.searchOptions = searchOptionsBean;
	}

	public void setServiceFilterRunt(ServiceFilterRunt serviceFilterRunt) {
		this.serviceFilterRunt = serviceFilterRunt;
	}

	public AutoBean<SnipBean> getSearchOptions() {
		return searchOptions;
	}
}
