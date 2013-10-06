package com.therdl.client.view;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

public interface SnipSearchView {

    public interface Presenter {
        void searchSnips(AutoBean<SnipBean> searchOptionsBean);
        void getInitialSnipList();
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();



    void showSnipList(JsArray<JSOModel> snips);

    void  setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    void doFilterSearch(AutoBean<SnipBean> searchOptionsBean);

    void getInitialSnipList();

}
