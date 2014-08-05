package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * User actioned on the snips paginator
 */
public class PaginationSnipsEvent extends GwtEvent<PaginationSnipsEventHandler> {
	public static Type<PaginationSnipsEventHandler> TYPE = new Type<PaginationSnipsEventHandler>();

	private boolean nextPage;
	private int pageIndex;

	public PaginationSnipsEvent (boolean nextPage, int pageIndex){
		this.nextPage = nextPage;
		this.pageIndex = pageIndex;
	}

	@Override
	public Type<PaginationSnipsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PaginationSnipsEventHandler eventHandler) {
		eventHandler.onPagination(this);
	}

	public boolean isNextPage() {
		return nextPage;
	}

	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
}
