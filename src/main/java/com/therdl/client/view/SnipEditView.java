package com.therdl.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.EditorClientWidget;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.List;

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
 */
public interface SnipEditView {

    public interface Presenter {
        void onDeleteSnip(String id);

        void submitBean(AutoBean<SnipBean> bean);

        void submitEditedBean(AutoBean<SnipBean> bean);
    }

    void setPresenter(Presenter presenter);

    void viewEditedSnip(AutoBean<SnipBean> snipBean);

    void setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    Widget asWidget();
}
