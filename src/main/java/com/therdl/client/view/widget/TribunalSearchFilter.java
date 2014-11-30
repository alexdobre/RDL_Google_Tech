package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.TribunalView;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.InlineCheckBox;

/**
 * The tribunal view search filter
 */
public class TribunalSearchFilter extends AbstractSearchFilter {

	interface TribunalSearchFilterUiBinder extends UiBinder<Widget, TribunalSearchFilter> {
	}

	private static TribunalSearchFilterUiBinder uiBinder = GWT.create(TribunalSearchFilterUiBinder.class);

	@UiField
	InlineCheckBox votingActive, votingInActive;

	private TribunalView view;

	public TribunalSearchFilter(TribunalView view) {
		initWidget(uiBinder.createAndBindUi(this));
		this.view = view;
	}

	@UiHandler("submit")
	public void onSubmit(ClickEvent event) {
		formSearchOptionBean();
		view.doFilterSearch();
	}

	@UiHandler(value = { "title" })
	public void onPassEnter(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			onSubmit(null);
		}
	}

	public void populateSearchOptions() {
		AutoBean<SnipBean> searchOptionsBean = view.getCurrentSearchOptionsBean();
		title.setText(searchOptionsBean.as().getTitle());
		dateFilterWidget.setDateFrom(searchOptionsBean.as().getDateFrom());
		dateFilterWidget.setDateTo(searchOptionsBean.as().getDateTo());
		if (searchOptionsBean.as().getVotingExpired() == null) {
			votingActive.setValue(true);
			votingInActive.setValue(true);
		} else {
			votingActive.setValue(!searchOptionsBean.as().getVotingExpired());
			votingInActive.setValue(searchOptionsBean.as().getVotingExpired());
		}
	}

	/**
	 * forms search option bean from filter form elements
	 *
	 * @return search option bean as SnipBean object
	 */
	private void formSearchOptionBean() {
		AutoBean<SnipBean> searchOptionsBean = view.getCurrentSearchOptionsBean();
		String titleText = title.getText();
		if (!titleText.equals("")) {
			searchOptionsBean.as().setTitle(titleText);
		} else {
			searchOptionsBean.as().setTitle(null);
		}

		if ((votingActive.getValue() && votingInActive.getValue()) || (!votingActive.getValue() && !votingInActive
				.getValue())) {
			searchOptionsBean.as().setVotingExpired(null);
		} else if (votingActive.getValue() && !votingInActive.getValue()) {
			searchOptionsBean.as().setVotingExpired(false);
		} else {
			searchOptionsBean.as().setVotingExpired(true);
		}

		String dateFromText = dateFilterWidget.getDateFrom();
		if (!dateFromText.equals("")) {
			searchOptionsBean.as().setDateFrom(dateFromText);
		} else {
			searchOptionsBean.as().setDateFrom(null);
		}

		String dateToText = dateFilterWidget.getDateTo();
		if (!dateToText.equals("")) {
			searchOptionsBean.as().setDateTo(dateToText);
		} else {
			searchOptionsBean.as().setDateTo(null);
		}

		view.setCurrentSearchOptionsBean(searchOptionsBean);
	}
}
