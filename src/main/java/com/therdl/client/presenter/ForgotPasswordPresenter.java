package com.therdl.client.presenter;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.Device;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.RDL;
import com.therdl.client.view.ForgotPassword;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;

import java.util.logging.Logger;

/**
 * ForgotPasswordPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 *
 * */

 public class ForgotPasswordPresenter implements ForgotPassword.Presenter {

    protected static Logger log = Logger.getLogger(ForgotPasswordPresenter.class.getName());
    private Beanery beanery = GWT.create(Beanery.class);
    private ForgotPassword view;

    public ForgotPasswordPresenter(ForgotPassword forgotPassword) {
        this.view = forgotPassword;
        this.view.setPresenter(this);
    }

    @Override
    public void doForgotPassword(String email) {
        AutoBean<AuthUserBean> bean = beanery.authBean();
        String forgotUrl = GWT.getModuleBaseURL() + "getSession";

        if (!Constants.DEPLOY) {
            forgotUrl = forgotUrl.replaceAll("/therdl", "");
        }

        bean.as().setEmail(email);
        bean.as().setAction("forgotPass");

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(forgotUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        String json = AutoBeanCodex.encode(bean).getPayload();

        try {
            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    log.info("ForgotPasswordPresenter doForgotPassword onResponseReceived json" + response.getText());
                    // deserialise the bean
                    AutoBean<AuthUserBean> authUserBean = AutoBeanCodex.decode(beanery, AuthUserBean.class, response.getText());
                    if(authUserBean != null && authUserBean.as().getEmail() != null) {
                        log.info(RDL.i18n.newPasswordSentToEmail());

                        view.getForgotPasswordPopup().hide();
                        view.getModal().show();
                    } else {
                        log.info(RDL.i18n.cannotFindEmailSorry());

                        view.getLabelEmailNotFound().setText(RDL.i18n.cannotFindEmailSorry());
                    }
                    view.getSubmitButton().setEnabled(true);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("ForgotPasswordPresenter onError)" + exception.getLocalizedMessage());

                }

            });

        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }  // end try


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