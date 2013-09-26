package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.widget.AppMenu;

public interface WelcomeView {

	public interface Presenter {

        void  doLogIn(String emailtxt, String passwordText);

	}

    void logout();

	void setPresenter(Presenter presenter);

	Widget asWidget();

    void showLoginFail();

    void  setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

}
