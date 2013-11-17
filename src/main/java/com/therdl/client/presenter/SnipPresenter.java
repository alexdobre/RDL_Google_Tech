package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.SnipView;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * SnipPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 * @ SnipView  snipView this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ String currentSnipId  used to retrieve the users correct snip
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
        this.snipView.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(snipView.asWidget());
    }

    /**
     * standard runtime method for MVP architecture
     *
     * @param container       the view container
     * @param currentUserBean the user state bean, mainly used for authorisation
     *                        and to update the menu
     */
    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(snipView.asWidget());
        if (controller.getCurrentUserBean().as().isAuth()) {
            snipView.setAppMenu(currentUserBean);
        }
        viewSnipById();

    }


    /**
     * find snip for the currentSnipId, while not a parameter the following variable
     * requires some explanation to help new developers
     * JSOModel data is a utility class for mapping javascript data objects and arrays
     * and storing them as a container to be used in a GWT java context
     * this is a home grown utility class written to encapsulate standard javascript to java
     * boiler plate code to keep main java code cleaner and more maintainable
     */
    private void viewSnipById() {
        log.info("SnipPresenter viewSnipById currentSnipId=" + currentSnipId);

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipPresenter viewSnipById  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean> currentBean = beanery.snipBean();
        currentBean.as().setAction("viewSnip");
        currentBean.as().setId(currentSnipId);

        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    log.info("getSnipResponse=" + response.getText());

                    AutoBean<SnipBean> bean = AutoBeanCodex.decode(beanery, SnipBean.class, response.getText());
                    snipView.viewSnip(bean);
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

    /**
     * send a request to the server to save reference for current snip
     * @param bean representing reference object
     */
    public void saveReference(AutoBean<SnipBean> bean) {
        log.info("SnipPresenter submit reference to server");
        bean.as().setAction("saveReference");
        final String refType = bean.as().getReferenceType();
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipPresenter submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");

        try {

            String json = AutoBeanCodex.encode(bean).getPayload();
            log.info("SnipPresenter submit json: " + json);
            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok now vaildate for dropdown
                        log.info("SnipPresenter submit post ok now validating");
                        snipView.saveReferenceResponseHandler(refType);
                        getSnipReferences("");
                    } else {
                        log.info("SnipPresenter submit post fail");
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipPresenter submit onError)" + exception.getLocalizedMessage());
                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }

    /**
     * get references for the current snip, creates bean objects from response json
     */
    public void getSnipReferences(final String referenceTypes) {
        log.info("SnipPresenter getSnipReferences currentSnipId=" + currentSnipId);

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipPresenter viewSnipById  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean> currentBean = beanery.snipBean();
        currentBean.as().setAction("getReferences");
        currentBean.as().setId(currentSnipId);
        currentBean.as().setReferenceType(referenceTypes);

        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    log.info("getSnipReferences=" + response.getText());

                    JsArray<JSOModel> data =
                            JSOModel.arrayFromJson(response.getText());

                    if (data.length() == 0) return;

                    ArrayList<JSOModel> jSonList = new ArrayList<JSOModel>();
                    ArrayList<AutoBean<SnipBean>> beanList = new ArrayList<AutoBean<SnipBean>>();

                    for (int i = 0; i < data.length(); i++) {
                        jSonList.add(data.get(i));
                        beanList.add(AutoBeanCodex.decode(beanery, SnipBean.class, jSonList.get(i).get(i+"")));

                    }

                    snipView.showReferences(beanList, referenceTypes);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipPresenter initialUpdate onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }

    /**
     * gives reputation to the current snip, increments reputation counter and saves user id to ensure giving reputation per user/snip only once
     */

    public void giveSnipReputation() {
        log.info("SnipPresenter giveSnipReputation currentSnipId=" + currentSnipId);

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipPresenter viewSnipById  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean> currentBean = beanery.snipBean();
        currentBean.as().setAction("giveRep");
        currentBean.as().setId(currentSnipId);

        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    log.info("giveSnipReputation=" + response.getText());

                    snipView.giveRepResponseHandler();
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipPresenter initialUpdate onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }
}
