package com.therdl.client.view;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 *
 * @ doForgotPassword(String email) submit user email for getting a new password if the email is valid.
 */
public interface ForgotPassword extends IsWidget {

    public interface Presenter {
        void doForgotPassword(String email);
        void showForgotPasswordPopup();
    }
    void setPresenter(Presenter presenter);

    PopupPanel getForgotPasswordPopup();
    HasText getLabelEmailNotFound();
    Button getSubmitButton();
    Modal getModal();
}
