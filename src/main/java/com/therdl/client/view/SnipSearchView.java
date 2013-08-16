package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

public interface SnipSearchView {

	public interface Presenter {
		
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

    void getSnipDemoResult(AutoBean<SnipBean> bean);
}
