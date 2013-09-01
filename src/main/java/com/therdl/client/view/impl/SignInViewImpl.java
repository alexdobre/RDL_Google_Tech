package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.http.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.view.SignInView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;

public class SignInViewImpl extends Composite implements SignInView {

    private static Logger log = Logger.getLogger("");

    private Presenter presenter;

    private boolean alreadyInit;

    private final WelcomeViewImpl welcomeViewImpl;

    private static SignInViewImplUiBinder uiBinder = GWT.create(SignInViewImplUiBinder.class);

    @UiField
    PasswordTextBox password;

    @UiField
    TextBox email;

    @UiField
    Button submit;



    @UiField
    Label loginFail;

    interface SignInViewImplUiBinder extends UiBinder<Widget, SignInViewImpl> {
    }

    public SignInViewImpl(WelcomeViewImpl welcomeView) {
        this.welcomeViewImpl =welcomeView;
        initWidget(uiBinder.createAndBindUi(this));
        password.setText("password");
        email.setText("Email");
        this.setStyleName("signInView");
        loginFail.setStyleName("logFail");
    }


    @UiHandler("password")
    public void onUserFocused(FocusEvent event) {
        password.setText("");   }

    @UiHandler("email")
    public void onEmailFocused(FocusEvent event) {
        email.setText("");   }



    @UiHandler("submit")
    public void onSubmit(ClickEvent event) {
        log.info("SignInViewImpl onSubmit");
        welcomeViewImpl.onSubmit();
    }


    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }


    public PasswordTextBox getPassword() {
        return password;
    }

    public TextBox getEmail() {
        return email;
    }

    @Override
    public Label getLoginFail() {
        return loginFail;
    }

    @Override
    public void setSignIsVisible(boolean state) {

        this.setVisible(state);

    }




}
