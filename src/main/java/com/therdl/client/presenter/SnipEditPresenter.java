package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.SnipEditView;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * SnipEditPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 * @ SnipEditView  view this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ List<JSOModel> jSonList  as a JSON Array of search results used to obtain a snip for editing
 * JSOModel objects, see com.therdl.shared.beans.JSOModel javadoc for this class
 * @ String currentSnipId  used to retrieve the users correct snip
 */

public class SnipEditPresenter implements Presenter, SnipEditView.Presenter, ValueChangeHandler<String> {

    private static Logger log = Logger.getLogger("");

    private final SnipEditView view;

    private Beanery beanery = GWT.create(Beanery.class);
    //    private List<JSOModel> jSon1List;
    private List<JSOModel> jSonList;
    private final AppController controller;
    private String currentSnipId;


    public SnipEditPresenter(SnipEditView view, String currentSnipId, AppController appController) {
        super();
        this.view = view;
        this.view.setPresenter(this);
        this.controller = appController;
        this.currentSnipId = currentSnipId;

        // user must be authorised to edit
        if (!controller.getCurrentUserBean().as().isAuth()) {
            History.newItem(RDLConstants.Tokens.WELCOME);
            History.fireCurrentHistoryState();

        }

    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());

        loadEditor();
        //  fetchSnips();
    }

    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(view.asWidget());
        // user must be authorised to edit
        if (controller.getCurrentUserBean().as().isAuth()) {
            log.info("SnipSearchPresenter go !controller.getCurrentUserBean().as().isAuth()  ");
            view.getAppMenu().setLogOutVisible(true);
            view.getAppMenu().setSignUpVisible(false);
            view.getAppMenu().setUserInfoVisible(true);
            view.setLoginResult(controller.getCurrentUserBean().as().getName(),
                    controller.getCurrentUserBean().as().getEmail(), true);
        }

        loadEditor();
        //   fetchSnips();

    }

    /*
     * loads snip editor for creating new snip when there is no selected snip
     * or send request to find snip with the current snip id
     */

    private void loadEditor() {
        if (!currentSnipId.equals("")) {
            findSnipById(currentSnipId);
        } else {
            view.setCurrentSnipBean(null);
        }
    }

    /*
     * send request to edit selected snip
     * @param bean snip bean to edit
     */

    @Override
    public void submitEditedBean(AutoBean<SnipBean> bean) {
        bean.as().setAction("update");
        log.info("SnipEditPresenter submitBean bean : " + bean.as().getTitle()+";snipType="+bean.as().getSnipType());
        log.info("SnipEditPresenter submit to server");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        // now submit to server
        try {

            String json = AutoBeanCodex.encode(bean).getPayload();
            log.info("SnipEditPresenter submit json: " + json);
            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok now vaildate for dropdown
                        log.info("SnipEditPresenter submit post ok now validating");
                        History.newItem(RDLConstants.Tokens.SNIPS);

                        //fetchSnips();

                    } else {
                        log.info("SnipEditPresenter submit post fail");

                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditPresenter submit onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }

    }

    // delete

    @Override
    public void onDeleteSnip(String id) {


        log.info("SnipEditPresenter onDelete: snip id " + id);
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter onDeleteSnip updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {
            AutoBean<SnipBean> actionBean = beanery.snipBean();
            actionBean.as().setAction("delete");
            actionBean.as().setId(id);
            String json = AutoBeanCodex.encode(actionBean).getPayload();

            log.info("SnipEditPresenter submit json: " + json);
            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok now vaildate for dropdown
                        log.info("SnipEditPresenter onDeleteSnip  ok now validating");
                        History.newItem(RDLConstants.Tokens.SNIPS);


                        //fetchSnips();

                    } else {
                        log.info("SnipEditPresenter onDeleteSnip fail");

                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditPresenter onDeleteSnip onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }


    }

    /*
     * send request to save new snip
     * @param bean new snip bean
     */
    @Override
    public void submitBean(AutoBean<SnipBean> bean) {
        bean.as().setAction("save");

        log.info("SnipEditPresenter submitBean bean : title : " + bean.as().getTitle());

        // now submit to server
        log.info("SnipEditPresenter submit to server");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");

        try {

            String json = AutoBeanCodex.encode(bean).getPayload();
            log.info("SnipEditPresenter submit json: " + json);
            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok now vaildate for dropdown
                        log.info("SnipEditPresenter submit post ok now validating");
                        History.newItem(RDLConstants.Tokens.SNIPS+":"+controller.getCurrentUserBean().as().getName());
                    } else {
                        log.info("SnipEditPresenter submit post fail");
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditPresenter submit onError)" + exception.getLocalizedMessage());
                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }
    }

    private void findSnipById(String snipId) {
        log.info("SnipEditPresenter findSnipById");

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if (!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter findSnipById  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean> currentBean = beanery.snipBean();
        currentBean.as().setAction("getSnip");
        currentBean.as().setId(snipId);

        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    log.info("getSnipResponse=" + response.getText());
                    AutoBean<SnipBean> bean = AutoBeanCodex.decode(beanery, SnipBean.class, response.getText());
                    view.viewEditedSnip(bean);
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

    public SnipEditView getView() {
        return view;
    }


    @Override
    public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
        log.info("SnipEditPresenter  onValueChange" + stringValueChangeEvent.getValue());
        if (stringValueChangeEvent.getValue().equals("snips")) {

            if (!controller.getCurrentUserBean().as().isAuth()) {
                History.newItem(RDLConstants.Tokens.WELCOME);
                History.fireCurrentHistoryState();

            }
        }
    }
}
