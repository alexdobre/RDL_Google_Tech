package com.therdl.client.presenter;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.therdl.client.view.ForgotPassword;
import com.therdl.client.view.impl.ForgotPasswordImpl;

/**
 * Created with IntelliJ IDEA.
 * User: markdiesta
 * Date: 3/11/14
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForgotPasswordPresenter implements ForgotPassword.Presenter {

    private ForgotPassword view;

    public ForgotPasswordPresenter(ForgotPassword forgotPassword) {
        this.view = forgotPassword;
        this.view.setPresenter(this);
    }

    @Override
    public void doForgotPassword(String email) {
        Window.alert("Your email is : " + email);
    }

    @Override
    public void showForgotPasswordPopup() {
        view.getForgotPasswordPopup().setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                int left = (Window.getClientWidth() - offsetWidth) >> 1;
                int top = (Window.getClientHeight() - offsetHeight) >> 4;
                view.getForgotPasswordPopup().setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(Window.getScrollTop() + top, 0));
            }
        });

    }

}