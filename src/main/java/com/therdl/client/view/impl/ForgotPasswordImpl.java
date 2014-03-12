package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
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

    public ForgotPasswordImpl() {
        super(true);
        add(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public PopupPanel getForgotPasswordPopup() {
        return this;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("btnSubmit")
    public void onSubmitClicked(ClickEvent event) {
        String email = txtBoxEmail.getText();
        if(presenter != null) {
            presenter.doForgotPassword(email);
        }
    }

    /**
     * get the email string
     *
     * @param event Standard GWT ClickEvent
     */
    @UiHandler("txtBoxEmail")
    public void onEmailFocused(FocusEvent event) {
        txtBoxEmail.setText("");
    }
}