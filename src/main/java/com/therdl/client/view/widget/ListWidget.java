package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SearchView;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;

import java.util.ArrayList;

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
			SnipListRow snipListRow = new SnipListRow(beanList.get(j), searchView.getCurrentUserBean());
			LinkedGroupItem listItem = new LinkedGroupItem();
			listItem.setPaddingBottom(2);
			listItem.setPaddingTop(2);
			listItem.setPaddingLeft(2);
			listItem.setPaddingRight(2);
			listItem.add(snipListRow);
			listGroup.add(listItem);
		}
	}
}