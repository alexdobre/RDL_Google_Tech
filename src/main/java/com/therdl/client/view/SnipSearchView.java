package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface SnipSearchView<T> {

	public interface Presenter<T> {
		
	}

	void setPresenter(Presenter<T> presenter);

	Widget asWidget();
}
