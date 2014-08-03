package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.SnipType;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * ListWidget class creates list of SnipListRow widgets with tabs for the given list of snips
 */
public class ListWidget extends Composite {
	interface ListWidgetUiBinder extends UiBinder<HTMLPanel, ListWidget> {
	}
	private static Logger log = Logger.getLogger("ListWidget");
	private static ListWidgetUiBinder ourUiBinder = GWT.create(ListWidgetUiBinder.class);

	@UiField
	AnchorListItem listRange, nextPage, prevPage;

	@UiField
	LinkedGroup listGroup;

	ArrayList<SnipListRow> itemList;

	public ListWidget(final SearchView searchView, ArrayList<AutoBean<SnipBean>> beanList, int pageIndex,
	                  String listRange) {
		initWidget(ourUiBinder.createAndBindUi(this));
		itemList = new ArrayList<SnipListRow>(Constants.DEFAULT_PAGE_SIZE);

		populateList(searchView, beanList, listRange);
	}

	public void populateList(SearchView searchView, ArrayList<AutoBean<SnipBean>> beanList, String listRange) {
		log.info("ListWidget populate list itemList: "+itemList+" size: "+(itemList==null?"null":itemList.size()));
		this.listRange.setText(listRange);

		for (int j = 0; j < beanList.size(); j++) {
			//we first see is we already have an item created
			SnipListRow snipListRow;
			if (itemList.size()>=j+1){
				//if yes we just populate the existing item
				snipListRow = itemList.get(j);
				snipListRow.populate(beanList.get(j), searchView.getCurrentUserBean(),
						SnipType.fromString(beanList.get(j).as().getSnipType()));
				ViewUtils.show(snipListRow.getParentObject());
			}else {
				//otherwise we create a new item
				LinkedGroupItem listItem = new LinkedGroupItem();
				snipListRow  = new SnipListRow(beanList.get(j), searchView.getCurrentUserBean(),
						SnipType.fromString(beanList.get(j).as().getSnipType()), listItem);
				itemList.add(snipListRow);
				listItem.setPaddingBottom(2);
				listItem.setPaddingTop(2);
				listItem.setPaddingLeft(2);
				listItem.setPaddingRight(2);
				listItem.add(snipListRow);
				listGroup.add(listItem);
			}
		}
		//finally we hide unused items
		hideUnusedItems(beanList);
	}

	public void nextPageActive(Boolean active) {
		nextPage.setActive(active);
	}

	public void prevPageActive(Boolean active) {
		prevPage.setActive(active);
	}

	public void hideUnusedItems(ArrayList<AutoBean<SnipBean>> beanList) {
		if (beanList.size() < Constants.DEFAULT_PAGE_SIZE){
			if (itemList.size() > beanList.size()){
				for (int i = 0; i< itemList.size()-beanList.size(); i++){
					ViewUtils.hide(itemList.get(beanList.size()+i).getParentObject());
				}
			}
		}
	}

}