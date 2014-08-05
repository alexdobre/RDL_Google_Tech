package com.therdl.client.view;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.AnchorListItem;

import com.therdl.client.view.widget.SnipListRow;

/**
 * A view that contains pagination
 */
public interface PaginatedView {

	public interface Presenter {
		public void nextPage();
		public void prevPage();
	}

	public void nextPageActive(Boolean active);

	public void prevPageActive(Boolean active);

	public AnchorListItem getListRange() ;

	public int getPageIndex() ;

	public ArrayList<SnipListRow> getItemList() ;

	public SearchView getSearchView() ;
}
