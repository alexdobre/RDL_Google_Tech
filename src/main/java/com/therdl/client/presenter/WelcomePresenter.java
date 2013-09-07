package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.SignInView;
import com.therdl.client.view.WelcomeView;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;

import java.util.logging.Logger;


public class WelcomePresenter implements Presenter, WelcomeView.Presenter {

    private static Logger log = Logger.getLogger("");


    private final WelcomeView welcomeView;

    private final AppController controller;


    public WelcomePresenter(WelcomeView welcomeView, AppController controller) {
        this.controller =controller;
        this.welcomeView = welcomeView;
        this.welcomeView.setPresenter(this);

    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(welcomeView.asWidget());
        if(!controller.getCurrentUserBean().as().isAuth() ) {
            log.info("WelcomePresenter go !controller.getCurrentUserBean().as().isAuth()");
            welcomeView.getAppMenu().setLogOutVisible(false);
            welcomeView.getAppMenu().setSignUpVisible(true);
            welcomeView.getAppMenu().setUserInfoVisible(false);
        }
    }


    public void doLogIn( String emailtxt, String passwordText ) {

        Beanery beanery = GWT.create(Beanery.class);
        log.info("v onSubmit password " + passwordText + " emailtxt  " + emailtxt);

        String authUrl = GWT.getModuleBaseURL() + "getSession";

        if(!Constants.DEPLOY){
            authUrl = authUrl.replaceAll("/therdl", "");
        }

        log.info("WelcomePresenter submit updateUrl: " + authUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(authUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {
            AutoBean<AuthUserBean> authBean = beanery.authBean();
            authBean.as().setPassword(passwordText);
            authBean.as().setEmail(emailtxt);
            authBean.as().setAction("auth");
            String json = AutoBeanCodex.encode(authBean).getPayload();

            log.info("WelcomePresenter submit json: " + json);
            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok move forward
                        log.info("WelcomePresenter onSubmit post ok");
                        log.info("WelcomePresenter onSubmit onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                        log.info("WelcomePresenter onSubmit onResponseReceived json" + response.getText());
                        JSOModel data = JSOModel.fromJson(response.getText());
                        String email = data.get("email");
                        String name = data.get("name");
                        boolean auth = data.getBoolean("auth");
                        if (!auth) {
                            log.info("WelcomePresenter onResponseReceived  !auth  ");
                            // use app menu

                        }   else {
                        controller.setCurrentUserBean(name, email, auth);
                        welcomeView.setloginresult(name, email, auth);
                            // use app menu

                        }
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


}
