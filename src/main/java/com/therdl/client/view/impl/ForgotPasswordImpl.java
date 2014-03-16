package com.therdl.client.view.impl;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.therdl.client.view.ForgotPassword;

/**
 * ForgotPasswordImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the RDL user created and voted features
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 */
public class ForgotPasswordImpl extends PopupPanel implements ForgotPassword {
    interface ForgotPasswordUiBinder extends UiBinder<Widget, ForgotPasswordImpl> {
    }

    private static ForgotPasswordUiBinder ourUiBinder = GWT.create(ForgotPasswordUiBinder.class);

    private Presenter presenter;

    @UiField
    TextBox txtBoxEmail;

    @UiField
    Label lblEmailNotFound;

    @UiField
    Button btnSubmit;

    @UiField
    Modal modalSuccessResetPassword;

    @UiField
    Modal modalFailResetPassword;

    public ForgotPasswordImpl() {
        super(true);
        add(ourUiBinder.createAndBindUi(this));
        lblEmailNotFound.setText("");
    }

    @Override
    public PopupPanel getForgotPasswordPopup() {
        return this;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public HasText getLabelEmailNotFound() {
        return lblEmailNotFound;
    }

    @Override
    public Button getSubmitButton() {
        return btnSubmit;
    }

    @Override
    public Modal getModalSuccessResetPassword() {
        return modalSuccessResetPassword;
    }

    @Override
    public Modal getModalFailResetPasswprd() {
        return modalFailResetPassword;
    }

    @UiHandler("btnSubmit")
    public void onSubmitClicked(ClickEvent event) {
        String email = txtBoxEmail.getText().trim();
        if(!email.isEmpty()) {
            if(presenter != null) {
                presenter.doForgotPassword(email);
            }
            btnSubmit.setEnabled(false);
        }
    }

    /**
     * get the email string
     *
     * @param event Standard GWT ClickEvent
     */
    @UiHandler("txtBoxEmail")
    public void onEmailFocused(FocusEvent event) {
        lblEmailNotFound.setText("");
    }
}