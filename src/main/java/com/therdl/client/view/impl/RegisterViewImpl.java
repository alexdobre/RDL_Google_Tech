package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.RegisterView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.FieldVerifier;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;


import java.util.logging.Logger;

/**
 * RegisterViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI so user can Registers/signs-up
 *
 * @ RegisterView.Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppMenu appMenu the upper menu view
 * fields below are standard GWT form fields for user sign-up
 * @ TextBox userName, email
 * @ PasswordTextBox psswd , cpsswd =='check password abbreviation for clarity'
 * @ Button submitbBtn
 */
public class RegisterViewImpl extends Composite implements RegisterView {

    private static Logger log = Logger.getLogger("");

    interface RegisterViewImplUiBinder extends UiBinder<Widget, RegisterViewImpl> {
    }

    private static RegisterViewImplUiBinder uiBinder = GWT.create(RegisterViewImplUiBinder.class);

    private RegisterView.Presenter presenter;

    private Beanery beanery = GWT.create(Beanery.class);


    @UiField
    AppMenu appMenu;

    @UiField
    TextBox userName;

    @UiField
    TextBox email;

    @UiField
    PasswordTextBox psswd;

    @UiField
    PasswordTextBox cpsswd;

    @UiField
    Button submitbBtn;

    private String username;
    private String password;
    private String eMail;

    public RegisterViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        appMenu.setSignUpView();
    }


    /**
     * Handler for form submit
     *
     * @param ClickEvent event Standard GWT ClickEvent
     *                   FieldVerifier static class for validation
     *                   AutoBean<AuthUserBean> newUserBean construct a bgean from supplied credentials
     *                   presenter.submitNewUser(newUserBean) submits bean for sign up in com.therdl.server.restapi.SessionServlet class
     */
    @UiHandler("submitbBtn")
    public void onSubmit(ClickEvent event) {
        log.info("RegisterViewImpl onSubmit verifying fields");

        username = userName.getText();
        password = psswd.getText();
        eMail = email.getText();

        log.info("RegisterViewImpl onSubmit verifying fields email.getText() " + email.getText());
        log.info("RegisterViewImpl onSubmit verifying fields eMail " + eMail);
        log.info("RegisterViewImpl onSubmit verifying fields password " + password);
        log.info("RegisterViewImpl onSubmit verifying fields username " + username);


        // can extend validation code here

        if (!FieldVerifier.isValidName(username)) {

            Window.alert("please enter valid username");


        }


        if (!FieldVerifier.isValidName(eMail)) {

            Window.alert("please enter valid email");


        }


        if (!FieldVerifier.isValidName(password)) {

            Window.alert("please enter valid password");


        }


        if (FieldVerifier.confirmPassword(psswd.getText(), cpsswd.getText())) {

            AutoBean<AuthUserBean> newUserBean = beanery.authBean();
            newUserBean.as().setAuth(false);
            newUserBean.as().setName(username);
            newUserBean.as().setEmail(eMail);
            newUserBean.as().setPassword(password);
            presenter.submitNewUser(newUserBean);

        } else Window.alert("Passwords do not match try again");

    }


    /**
     * Sets the upper header Menu to the correct state for supplied credentials
     * post sign up called from presenter
     *
     * @param String  name supplied credential
     * @param String  email supplied credential
     * @param boolean auth  auth state from server via presenter
     */
    @Override
    public void setLoginResult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true " + name);

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(name);
            this.appMenu.setLogInVisible(false);
        } else {
            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }

    }


    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
