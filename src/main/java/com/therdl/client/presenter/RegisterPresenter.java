package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.RegisterView;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * user signs up for application here
 * simple but good looking formv iew
 */
public class RegisterPresenter implements Presenter, RegisterView.Presenter {

    private static Logger log = Logger.getLogger("");
    private RegisterView registerView;
    private final AppController controller;
    // idea here is to use a unifome object structure in the client to help new devs get up to sppeed
    // ie they only need to learn one block of code and it is the same wherever the client updates
    private Beanery beanery = GWT.create(Beanery.class);


    public RegisterPresenter(RegisterView registerView, AppController appController) {
        this.controller = appController;
        this.registerView = registerView;
        registerView.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(registerView.asWidget());

    }

    @Override
    public void submitNewUser(AutoBean<AuthUserBean> bean) {
        log.info("RegisterPresenter submitNewUser with  bean as ");
        log.info(AutoBeanCodex.encode(bean).getPayload());

        String updateUrl = GWT.getModuleBaseURL() + "getSession";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("RegisterPresenter submitNewUser with  updateUrl: " + updateUrl);

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        bean.as().setAction("signUp");
        String json = AutoBeanCodex.encode(bean).getPayload();

        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    log.info("RegisterPresenter submitNewUser  onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                    log.info("RegisterPresenter submitNewUser onResponseReceived json" + response.getText());

                    AutoBean<AuthUserBean> bean = AutoBeanCodex.decode(beanery, AuthUserBean.class, response.getText());

                    log.info("RegisterPresenter submitNewUser bean.as().getName() " + bean.as().getName());
                    log.info("RegisterPresenter submitNewUser bean.as().getEmail() " + bean.as().getEmail());
                    log.info("RegisterPresenter submitNewUser bean.as().getAction() " + bean.as().getAction());
                    log.info("RegisterPresenter submitNewUser bean.as().isAuth() " + bean.as().isAuth());

                    controller.setCurrentUserBean(bean.as().getName(), bean.as().getEmail(), true);

                    History.newItem(RDLConstants.Tokens.WELCOME);

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("UpdateServiceImpl initialUpdate onError)" + exception.getLocalizedMessage());

                }

            });

        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }  // end try
    }
}
