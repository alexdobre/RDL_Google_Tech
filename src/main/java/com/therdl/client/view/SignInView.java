package com.therdl.client.view;

import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * see com.therdl.client.view.impl.SignInViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * @ PasswordTextBox getPassword() standard form processing method, gets the Password form field
 * @ TextBox getEmail() standard form processing method, gets the Email form field
 * @ setSignIsVisible(boolean state) sets the menu/Nav-bar when user can log in
 * @ Label getLoginFail() displays the validation method when the login fails eg password incorrect
 */
public interface SignInView extends RdlView {

	String getPassword();

	org.gwtbootstrap3.client.ui.TextBox getEmail();

	org.gwtbootstrap3.client.ui.Label getLoginFail();
}