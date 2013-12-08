package com.therdl.client.view;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;


/**
 * see com.therdl.client.view.impl.ProfileViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ AppMenu getAppMenu() returns the Nav-bar header using the user authorisation status
 * this method sets the options in the header/nav bar AppMenu widget
 * @ setloginresult(String name, String email, boolean auth) sets the options in the header/nav-bar
 * using the user's authorisation status
 * @ void showSnipList(JsArray<JSOModel> snips)  display's the search result list as a JSON Array of
 * JSOModel objects, see com.therdl.shared.beans.JSOModel javadoc for this class
 * @ getInitialSnipList() display's the initial search result list
 */
public interface SnipSearchView {

    public interface Presenter {
        void searchSnips(AutoBean<SnipBean> searchOptionsBean, int pageIndex);

        void getInitialSnipList(int pageIndex);
    }

    void setPresenter(Presenter presenter);

    Widget asWidget();

   // void showSnipList(JsArray<JSOModel> snips);

    void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

    void setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    void doFilterSearch(AutoBean<SnipBean> searchOptionsBean, int pageIndex);

    void getInitialSnipList(int pageIndex);

}
