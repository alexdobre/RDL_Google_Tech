package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * see com.therdl.client.view.impl.SignInViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * @ setAppMenu(AutoBean<CurrentUserBean> currentUserBean) using the user authorisation status
 * this method sets the options in the header/nav bar
 * @ PasswordTextBox getPassword() standard form processing method, gets the Password form field
 * @ TextBox getEmail() standard form processing method, gets the Email form field
 * @ setSignIsVisible(boolean state) sets the menu/Nav-bar when user can log in
 * @ Label getLoginFail() displays the validation method when the login fails eg password incorrect
 */
public interface SignInView extends IsWidget {

    interface Presenter {
    }

    void setPresenter(Presenter presenter);

    PasswordTextBox getPassword();

    TextBox getEmail();

    void setSignIsVisible(boolean state);

    Label getLoginFail();


}