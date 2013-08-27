package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.SnipBean;

public interface SnipSearchView {

	public interface Presenter {
		
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

    void getSnipDemoResult(AutoBean<SnipBean> bean);


    void  setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();
}
