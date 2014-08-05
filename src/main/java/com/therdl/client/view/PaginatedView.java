package com.therdl.client.view;

/**
 * A view that contains pagination
 */
public interface PaginatedView {

	public void nextPageActive(boolean active);

	public void prevPageActive(boolean active);

	public void setListRange(String listRange);

}
