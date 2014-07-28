package com.therdl.client.view.widget;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.AnchorListItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.SearchView;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.SnipBean;

/**
 * ListWidget class creates list of SnipListRow widgets with tabs for the given list of snips
 */
public class ListWidget extends Composite {
	interface ListWidgetUiBinder extends UiBinder<HTMLPanel, ListWidget> {
	}

	private static ListWidgetUiBinder ourUiBinder = GWT.create(ListWidgetUiBinder.class);


	@UiField
	HTMLPanel rootPanel;

	@UiField
	AnchorListItem listRange;

	public ListWidget(final SearchView searchView, ArrayList<AutoBean<SnipBean>> beanList, int pageIndex,
			String listRange) {
		initWidget(ourUiBinder.createAndBindUi(this));

		this.listRange.setText(listRange);

		// default size of rows in one tab
		int listRowSize = Constants.DEFAULT_PAGE_SIZE;
		int tabCount = 1;
		if (beanList.size() != 0) {
			tabCount = (int)Math.ceil((double)beanList.get(0).as().getCount() / listRowSize);
		}

		TabPanel tabPanel = new TabPanel();

		// creates tabs of count tabCount
		for (int i = 1; i <= tabCount; i++) {
			// creates content of current tab
			FlowPanel tabContent = new FlowPanel();
			if (beanList.size() == 0) {
				tabContent.add(new Label(RDL.i18n.noDataToDisplay()));
			}

			tabPanel.add(tabContent, i + "");

		}

		//select first tab

		for (int j = 0; j < beanList.size(); j++) {
			SnipListRow snipListRow = new SnipListRow(beanList.get(j), searchView.getCurrentUserBean(), false);
			((FlowPanel)tabPanel.getWidget(pageIndex)).add(snipListRow);
		}

		tabPanel.selectTab(pageIndex);

		tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> integerBeforeSelectionEvent) {
				if (searchView.getCurrentSearchOptionsBean() != null) {
					searchView.doFilterSearch(searchView.getCurrentSearchOptionsBean(), integerBeforeSelectionEvent.getItem());
				} else {
					searchView.getInitialSnipList(integerBeforeSelectionEvent.getItem());
				}
			}
		});

		rootPanel.add(tabPanel);
	}
}