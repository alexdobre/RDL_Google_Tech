package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.RegisterView;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.*;

import java.util.logging.Logger;

/**
 * user signs up for application here
 * simple but good looking formv view
 */
public class RegisterPresenter implements Presenter, RegisterView.Presenter {

    private static Logger log = Logger.getLogger("");
    private RegisterView registerView;
    private final AppController controller;
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
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(registerView.asWidget());

    }

    @Override
    public void submitNewUser(AutoBean<AuthUserBean> bean) {
        log.info(AutoBeanCodex.encode(bean).getPayload());
        String updateUrl = GWT.getModuleBaseURL() + "getSession";
         // handle jboss urls for deploy
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

                    log.info("RegisterPresenter submitNewUser onResponseReceived json" + response.getText());
                    // deserialise the bean
                    AutoBean<AuthUserBean> bean = AutoBeanCodex.decode(beanery, AuthUserBean.class, response.getText());
                   // on success user is authorised on sign up
                    controller.setCurrentUserBean(bean.as().getName(), bean.as().getEmail(), true);
                     // return to welcome page
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
