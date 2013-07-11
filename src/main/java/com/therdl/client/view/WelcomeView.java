package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface WelcomeView {

	public interface Presenter {
		
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();
}
