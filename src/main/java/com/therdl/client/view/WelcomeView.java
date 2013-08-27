package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.widget.AppMenu;

public interface WelcomeView {

	public interface Presenter {

        void  doLogIn();

	}

    void logout();

	void setPresenter(Presenter presenter);

	Widget asWidget();

    SignInView getSignInView();

    void  setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

}
