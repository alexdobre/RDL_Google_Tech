package com.therdl.client.view;

import com.google.gwt.user.client.ui.Panel;

/**
 * The user searches for site content
 */
public interface ContentSearchView extends RdlView {

	public interface Presenter {
	}

	public void setPresenter(ContentSearchView.Presenter presenter);

	public Panel getResultsContainer();
}
