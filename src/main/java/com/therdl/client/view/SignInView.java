package com.therdl.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public interface SignInView extends IsWidget {

     interface Presenter {

    void goTo(Place place);
    void setRunning(boolean running);

     }

    void setPresenter(Presenter presenter);

    PasswordTextBox getPassword();

    TextBox getEmail();

}