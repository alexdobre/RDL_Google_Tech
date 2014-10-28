package com.therdl.client.logic;

import java.util.ArrayList;
import java.util.List;

import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.widget.runtized.SortBit;

/**
 * Logic behind the sort bits that are interconnected
 */
public class LinkedSortBit {

	private List<Link> linkedList;
	private int sortOrder;
	private String activeSortParam;

	public LinkedSortBit() {
		linkedList = new ArrayList<Link>();
		sortOrder = 1;
	}

	/**
	 * Acts on the linked sort bit list, either by making the given sort bit active
	 * or by changing the sort order if bit already active
	 *
	 * @param sortBit the given sort bit
	 */
	public void sortOrderAction(SortBit sortBit) {
		Link givenLink = null;
		for (Link l : linkedList) {
			if (l.getSortBit().equals(sortBit)) {
				givenLink = l;
				break;
			}
		}

		if (givenLink != null) {
			//if the current bit is already active we change the sort order
			if (givenLink.getSortBit().isActive()) {
				givenLink.setSortOrder(invertSortOrder(givenLink.getSortOrder()));
				sortOrder = givenLink.getSortOrder();
				givenLink.getSortBit().setSortOrder(sortOrder);
				//otherwise we make the current bit active
			} else {
				allInactive();
				givenLink.getSortBit().setActive(true);
				sortOrder = givenLink.getSortOrder();
				activeSortParam = givenLink.getSortParam();
			}
		}
	}

	public void passRunt(ServiceFilterRunt runt) {
		for (Link l : linkedList) {
			l.getSortBit().setRunt(runt);
		}
	}

	private int invertSortOrder(int sortOrder) {
		if (sortOrder == 1)
			return -1;
		else
			return 1;
	}

	private void allInactive() {
		for (Link l : linkedList) {
			l.getSortBit().setActive(false);
		}
	}

	public String getActiveSortParam() {
		return activeSortParam;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * Searches for and sets the active sort param
	 *
	 * @param activeSortParam
	 */
	public void setActiveSortParam(String activeSortParam) {
		this.activeSortParam = activeSortParam;
		allInactive();
		for (Link l : linkedList) {
			if (l.getSortParam().equals(activeSortParam)) {
				l.setSortOrder(sortOrder);
				l.getSortBit().setActive(true);
				break;
			}
		}
	}

	/**
	 * Holds a link between a SortBit and it's sort parameter
	 */
	public static class Link {
		private SortBit sortBit;
		private String sortParam;
		private int sortOrder;

		public Link(SortBit sortBit, String sortParam, int sortOrder) {
			this.sortBit = sortBit;
			this.sortParam = sortParam;
		}

		public SortBit getSortBit() {
			return sortBit;
		}

		public void setSortBit(SortBit sortBit) {
			this.sortBit = sortBit;
		}

		public String getSortParam() {
			return sortParam;
		}

		public void setSortParam(String sortParam) {
			this.sortParam = sortParam;
		}

		public int getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(int sortOrder) {
			this.sortOrder = sortOrder;
		}
	}

	public List<Link> getLinkedList() {
		return linkedList;
	}

	public void setLinkedList(List<Link> linkedList) {
		this.linkedList = linkedList;
	}
}
