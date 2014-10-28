package com.therdl.client.presenter.runt;

import com.therdl.client.view.widget.runtized.SortBit;

/**
 * Functionality behind the service filter
 */
public interface ServiceFilterRunt {

	/**
	 * Forms the search options from the user filter and executes the search
	 */
	public void filterSearch();

	/**
	 * The user activates a new sort bit or reactivates the existing one
	 *
	 * @param sortBit
	 */
	public void newSortAction(SortBit sortBit);
}
