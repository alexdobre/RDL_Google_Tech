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
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;

/**
 * ListWidget class creates list of SnipListRow widgets with tabs for the given list of snips
 */
public class ListWidget extends Composite {
	interface ListWidgetUiBinder extends UiBinder<HTMLPanel, ListWidget> {
	}

	private static ListWidgetUiBinder ourUiBinder = GWT.create(ListWidgetUiBinder.class);

	@UiField
	AnchorListItem listRange, nextPage, prevPage;

	@UiField
	LinkedGroup listGroup;

	public ListWidget(final SearchView searchView, ArrayList<AutoBean<SnipBean>> beanList, int pageIndex,
			String listRange) {
		initWidget(ourUiBinder.createAndBindUi(this));

		this.listRange.setText(listRange);

		for (int j = 0; j < beanList.size(); j++) {
			SnipListRow snipListRow = new SnipListRow(beanList.get(j), searchView.getCurrentUserBean(), false);
			LinkedGroupItem listItem = new LinkedGroupItem();
			listItem.add(snipListRow);
			listGroup.add(listItem);
		}
	}
}