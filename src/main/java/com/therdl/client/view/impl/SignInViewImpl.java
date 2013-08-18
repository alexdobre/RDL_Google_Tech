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

    interface SignInViewImplUiBinder extends UiBinder<Widget, SignInViewImpl> {
    }

    public SignInViewImpl(WelcomeViewImpl welcomeView) {
        this.welcomeViewImpl =welcomeView;
        initWidget(uiBinder.createAndBindUi(this));
        password.setText("password");
        email.setText("Email");
    }


    @UiHandler("password")
    public void onUserFocused(FocusEvent event) {
        password.setText("");   }

    @UiHandler("email")
    public void onEmailFocused(FocusEvent event) {
        email.setText("");   }

    @UiHandler("submit")
    public void onSubmit(ClickEvent event) {
        Beanery beanery = GWT.create(Beanery.class);
        String passwordText = password.getText();
        String emailtxt =   email.getText();
        log.info("SignInViewImpl onSubmit password " + password + " emailtxt  " + emailtxt);

        String authUrl = GWT.getModuleBaseURL() + "getSession";
        authUrl = authUrl.replaceAll("/therdl", "");

        log.info("SnipEditorWorkflow submit updateUrl: " + authUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(authUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {
            AutoBean<AuthUserBean> authBean = beanery.authBean();
            authBean.as().setPassword(passwordText);
            authBean.as().setEmail(emailtxt);
            String json = AutoBeanCodex.encode(authBean).getPayload();

            log.info("SnipEditorWorkflow submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok move forward
                        log.info("SignInViewImpl onSubmit post ok");
                        log.info("SignInViewImpl onSubmit onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                        log.info("SignInViewImpl onSubmit onResponseReceived json" + response.getText());
                        JSOModel data =  JSOModel.fromJson(response.getText());
                        String email = data.get("email");
                        String name = data.get("name");
                        boolean auth = data.getBoolean("auth");
                        welcomeViewImpl.setloginresult(name, email, auth);

                    } else {
                        log.info("SignInViewImpl onSubmit  post fail");

                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SignInViewImpl onSubmit onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }


    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }


}
