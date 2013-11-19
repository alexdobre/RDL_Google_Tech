package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.widget.AppMenu;

/**
 * see com.therdl.client.view.impl.WelcomeViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ showLoginFail() displays the validation method when the login fails eg password incorrect
 * @ AppMenu getAppMenu() returns the Nav-bar header using the user authorisation status
 * @ setloginresult(String name, String email, boolean auth) sets the options in the header/nav bar
 * using the user sign up result status
 */
public interface WelcomeView {

    public interface Presenter {

        void doLogIn(String emailtxt, String passwordText);

    }

    void logout();

    void setPresenter(Presenter presenter);

    Widget asWidget();

    void showLoginFail();

    void setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    void init();

}
