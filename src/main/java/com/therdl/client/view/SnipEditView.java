package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface SnipEditView<T> {

	public interface Presenter<T> {

		void onSaveButtonClicked();

		void onCloseButtonClicked();
	}

	void setPresenter(Presenter<T> presenter);

	Widget asWidget();
}
