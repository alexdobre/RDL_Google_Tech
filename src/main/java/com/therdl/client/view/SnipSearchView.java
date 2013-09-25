package com.therdl.client.view;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

public interface SnipSearchView {

    void updateListWidget(JsArray<JSOModel> snips, int pSize);

    public interface Presenter {
        void searchSnips(AutoBean<SnipBean> searchOptionsBean);
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();



    void getSnipListDemoResult(JsArray<JSOModel> snips);

    void  setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    /**
     * searches for snips based on 'match'
     * @param match : title of the snip currently
     */
    void searchSnips(String match);

    void doFilterSearch(AutoBean<SnipBean> searchOptionsBean);

}
