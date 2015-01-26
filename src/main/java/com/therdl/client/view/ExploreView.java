package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * The explore view shown to new comers
 * @author alex
 */
public interface ExploreView extends RdlView {

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

}