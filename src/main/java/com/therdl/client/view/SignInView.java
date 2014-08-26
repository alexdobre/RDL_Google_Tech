package com.therdl.client.view;

import org.gwtbootstrap3.client.ui.TextBox;

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
public interface SignInView extends RdlView, ValidatedView {

	String getPassword();

	TextBox getEmail();

}