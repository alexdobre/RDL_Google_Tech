package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.SnipSearchView;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.*;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * SnipSearchPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 * @ SnipSearchView  snipSearchView this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ String currentSnipId  used to retrieve the users correct snip
 * @ AutoBean<SnipBean>  currentBean determines the authorisation state of the snipSearchView when
 * presented
 * @ArrayList<JSOModel> jSonList, List of  JSOModel objects, see com.therdl.shared.beans.JSOModel javadoc for this class
 * @ void getInitialSnipList() gets the initila list of snips when view first comes into focus
 * @ searchSnips(final AutoBean<SnipBean> searchOptionsBean) performs a snip search
 */


public class SnipSearchPresenter implements Presenter, SnipSearchView.Presenter {
    private static Logger log = Logger.getLogger("");
    private final SnipSearchView snipSearchView;
    private Beanery beanery = GWT.create(Beanery.class);
    private ArrayList<JSOModel> jSonList;
    private AutoBean<SnipBean> currentBean;
    private final AppController controller;

    public SnipSearchPresenter(SnipSearchView snipSearchView, AppController controller) {
        this.controller = controller;
        this.snipSearchView = snipSearchView;
        this.snipSearchView.setPresenter(this);
        log.info("SnipSearchPresenter constructor");
        // anyone can view snips no auth code needed

    }

    @Override
    public void go(HasWidgets container) {
        log.info("SnipSearchPresenter go adding view");
        container.clear();
        container.add(snipSearchView.asWidget());
    }

    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        log.info("SnipSearchPresenter go adding view");
        container.clear();
        container.add(snipSearchView.asWidget());
        // use auth code here to handle app menu options
        if (controller.getCurrentUserBean().as().isAuth()) {
            log.info("SnipSearchPresenter go !controller.getCurrentUserBean().as().isAuth()  ");
            snipSearchView.getAppMenu().setLogOutVisible(true);
            snipSearchView.getAppMenu().setSignUpVisible(false);
            snipSearchView.getAppMenu().setUserInfoVisible(true);
            snipSearchView.setloginresult(controller.getCurrentUserBean().as().getName(),
                    controller.getCurrentUserBean().as().getEmail(), true);
            //  getInitialList();
        } else {
            snipSearchView.getAppMenu().setUserInfoVisible(false);
            snipSearchView.getAppMenu().setUserInfoVisible(false);
            //     getInitialList();
        }

    }

    /**
     * Polls the server for the list of snips, uses a getAll/findAll type crud on the backend
     */
    public void getInitialSnipList() {
        log.info("SnipSearchPresenter getInitialList");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipSearchPresenter getInitialList  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        currentBean = beanery.snipBean();
        currentBean.as().setAction("getall");
        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    log.info("SnipSearchPresenter  onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                    log.info("SnipSearchPresenter onResponseReceived json" + response.getText());

                    JsArray<JSOModel> data =
                            JSOModel.arrayFromJson(response.getText());

                    if (data.length() == 0) return;

                    jSonList = new ArrayList<JSOModel>();

                    for (int i = 0; i < data.length(); i++) {
                        jSonList.add(data.get(i));
                    }

                    log.info("SnipSearchPresenter  initialUpdate onResponseReceived json" + jSonList.get(0).get("0"));

                    AutoBean<SnipBean> bean = AutoBeanCodex.decode(beanery, SnipBean.class, jSonList.get(0).get("0"));

                    log.info("" + bean.as().getTitle());
                    log.info("" + bean.as().getAuthor());

                    snipSearchView.showSnipList(data);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("UpdateServiceImpl initialUpdate onError)" + exception.getLocalizedMessage());
                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }    // end initialUpdate method

    /**
     * Handles snips searching request | response
     *
     * @param searchOptionsBean : bean of the snip search options
     */
    @Override
    public void searchSnips(final AutoBean<SnipBean> searchOptionsBean) {
        log.info("SnipSearchPresenter getSnipSearchResult");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipSearchPresenter getSnipDemoResult  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");

        searchOptionsBean.as().setAction("search");

        log.info("dateFrom=" + searchOptionsBean.as().getDateFrom() + ";dateTo=" + searchOptionsBean.as().getDateTo());

        String json = AutoBeanCodex.encode(searchOptionsBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    log.info("SnipSearchPresenter  onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                    log.info("SnipSearchPresenter onResponseReceived json" + response.getText());

                    JsArray<JSOModel> data =
                            JSOModel.arrayFromJson(response.getText());

                    //    if(data.length() == 0 )  return;

                    jSonList = new ArrayList<JSOModel>();

                    for (int i = 0; i < data.length(); i++) {
                        jSonList.add(data.get(i));
                    }

                    snipSearchView.showSnipList(data);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("UpdateServiceImpl initialUpdate onError)" + exception.getLocalizedMessage());
                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }    // end initialUpdate method


}
