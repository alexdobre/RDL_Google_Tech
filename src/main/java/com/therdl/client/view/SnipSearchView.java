package com.therdl.client.view;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

public interface SnipSearchView {

    void updateListWidget(JsArray<JSOModel> snips);

    public interface Presenter {
        /**
         * the presenter implementation of snips search
         * basically makes an ajax call to the server to search the snips based on 'match'
         * @param match : title of the snip currently
         */
        void searchSnips(String match);
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

    void getSnipDemoResult(AutoBean<SnipBean> bean);

    void getSnipListDemoResult(JsArray<JSOModel> snips);

    void  setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    /**
     * searches for snips based on 'match'
     * @param match : title of the snip currently
     */
    void searchSnips(String match);
}
