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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.RDL;
import com.therdl.client.view.SignInView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.LoginHandler;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RequestObserver;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogInOkEvent;
import com.therdl.shared.events.LogInOkEventEventHandler;

import java.util.logging.Logger;


/**
 * SignInViewImpl extends PopupPanel is a pop up embedded in the Welcome view
 * <p/>
 * fields below are standard GWT form fields for user sign-up
 *
 * @ TextBox  email  String used as the unique identifier in the database
 * @ PasswordTextBox psswd  String the user password
 * @ Label loginFail  GWT Label widget validation callback for login fails
 */

public class SignInViewImpl extends PopupPanel implements SignInView {

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
        super(true);
        add(uiBinder.createAndBindUi(this));
        this.welcomeViewImpl = welcomeView;
        password.setText("password");
        email.setText("Email");
        this.setStyleName("signInView");

        // user has just successfully logged in update app menu
        GuiEventBus.EVENT_BUS.addHandler(LogInOkEvent.TYPE, new LogInOkEventEventHandler() {

            @Override
            public void onLogInOkEvent(LogInOkEvent onLoginOkEvent) {
                hide();
            }
        });


    }

    /**
     * get the password string
     *
     * @param event Standard GWT ClickEvent
     */
    @UiHandler("password")
    public void onUserFocused(FocusEvent event) {
        password.setText("");
    }

    /**
     * get the email string
     *
     * @param event Standard GWT ClickEvent
     */
    @UiHandler("email")
    public void onEmailFocused(FocusEvent event) {
        email.setText("");
    }

    /**
     * Handler for form submit
     *
     * @param event Standard GWT ClickEvent
     *              <p/>
     *              welcomeViewImpl.onSubmit(eMail, psswd ) submits bean for sign-in in
     *              com.therdl.server.restapi.SessionServlet class
     */
    @UiHandler("submit")
    public void onSubmit(ClickEvent event) {
        log.info("SignInViewImpl onSubmit");

        String eMail = email.getText();
        String password = this.password.getText();

        log.info("SignInViewImpl onSubmit eMail password " + eMail + " : " + password);

        if (eMail != null && !eMail.equals("Email") && password != null && !this.password.equals("password")) {
            welcomeViewImpl.onSubmit(eMail, password);

        } else {
            loginFail.setVisible(true);
            loginFail.setText(RDL.i18n.loginFailMsg1());

        }
    }

    /**
     * click handler for sign up link, redirects to sign up view
     * @param event
     */
    @UiHandler("signUpLink")
    public void onSignUpLinkClicked(ClickEvent event) {
        History.newItem(RDLConstants.Tokens.SIGN_UP);
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
