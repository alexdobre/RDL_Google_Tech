package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.widget.SnipView;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.Map;
import java.util.logging.Logger;

/**
 * SnipPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 *  @ SnipView  snipView this presenter GUI component
 *  @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 *  @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 *  @ String currentSnipId  used to retrieve the users correct snip
 */
public class SnipPresenter implements Presenter, SnipView.Presenter {

    private static Logger log = Logger.getLogger("");

    private SnipView snipView;
    private Beanery beanery = GWT.create(Beanery.class);
    private String currentSnipId;
    private AppController controller;

    public SnipPresenter(SnipView snipView, String currentSnipId, AppController appController) {
        this.snipView = snipView;
        this.currentSnipId = currentSnipId;
        this.controller = appController;
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(snipView.asWidget());
    }

    /**
     *  standard runtime method for MVP architecture
     * @param container  the view container
     * @param currentUserBean the user state bean, mainly used for authorisation
     * and to update the menu
     */
    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(snipView.asWidget());
        if(controller.getCurrentUserBean().as().isAuth() ) {
            snipView.setAppMenu(currentUserBean);
            log.info("go function");
            viewSnipById();
        }
    }


    /**
     *  find snip for the currentSnipId, while not a parameter the following variable
     *  requires some explanation to help new developers
     *  JSOModel data is a utility class for mapping javascript data objects and arrays
     *  and storing them as a container to be used in a GWT java context
     *  this is a home grown utility class written to encapsulate standard javascript to java
     *  boiler plate code to keep main java code cleaner and more maintainable
     */
    private void viewSnipById() {
        log.info("SnipPresenter viewSnipById currentSnipId="+currentSnipId);

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if(!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipPresenter viewSnipById  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean>  currentBean = beanery.snipBean();
        currentBean.as().setAction("viewSnip");
        currentBean.as().setId(currentSnipId);

        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    log.info("getSnipResponse="+response.getText());
                    JSOModel data = JSOModel.fromJson(response.getText());

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditPresenter initialUpdate onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }
}
