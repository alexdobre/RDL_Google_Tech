package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

public interface SnipSearchView<T> {

	public interface Presenter<T> {
		
	}

	void setPresenter(Presenter<T> presenter);

	Widget asWidget();

    void getSnipDemoResult(AutoBean<SnipBean> bean);
}
