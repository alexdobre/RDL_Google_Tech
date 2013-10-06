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

public class SnipEditPresenter implements Presenter, SnipEditView.Presenter , ValueChangeHandler<String> {

    private static Logger log = Logger.getLogger("");

    private final SnipEditView view;

    private Beanery beanery = GWT.create(Beanery.class);
//    private List<JSOModel> jSon1List;
    private List<JSOModel> jSonList;
    private final AppController controller;



    public SnipEditPresenter(SnipEditView view, AppController appController) {
        super();
        this.view = view;
        this.view.setPresenter(this);
        this.controller =appController;
        // user must be authorised to edit
        if(!controller.getCurrentUserBean().as().isAuth() ) {
            History.newItem(RDLConstants.Tokens.WELCOME);
            History.fireCurrentHistoryState();

        }

    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        fetchSnips();
    }

    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(view.asWidget());
        // user must be authorised to edit
        if(controller.getCurrentUserBean().as().isAuth() ) {
            log.info("SnipSearchPresenter go !controller.getCurrentUserBean().as().isAuth()  ");
            view.getAppMenu().setLogOutVisible(true);
            view.getAppMenu().setSignUpVisible(false);
            view.getAppMenu().setUserInfoVisible(true);
            view.setloginresult(controller.getCurrentUserBean().as().getName(),
                    controller.getCurrentUserBean().as().getEmail(), true  );
        }
        fetchSnips();

    }

    @Override
    public void submitEditedBean(AutoBean<SnipBean> bean) {

        // get snip data from EditorClient widget
        String title = view.getEditorClientWidget().getSnipData().get("title");
        String contentAsText = view.getEditorClientWidget().getSnipData().get("contentAsString");
        String contentAsHtml = view.getEditorClientWidget().getSnipData().get("contentAsHtml");
        String coreCat = view.getEditorClientWidget().getSnipData().get("coreCat");
        String subCat = view.getEditorClientWidget().getSnipData().get("subCat");

        if (bean.as().getId() == null) {
            Window.alert("this is a new snip please use submit not submit-edit" );
            return;
        }

        log.info("SnipEditPresenter submitEditBean bean");
        bean.as().setTitle(title);
        bean.as().setCoreCat(coreCat);
        bean.as().setSubCat(subCat);
        bean.as().setContent(contentAsText);
        bean.as().setAuthor(controller.getCurrentUserBean().as().getName());
        bean.as().setAction("update");
        log.info("SnipEditPresenter submitBean bean : " +  bean.as().getTitle());
        log.info("SnipEditPresenter submit to server");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if(!Constants.DEPLOY){
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        // now submit to server
        try {

            String json = AutoBeanCodex.encode( bean).getPayload();
            log.info("SnipEditPresenter submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok now vaildate for dropdown
                        log.info("SnipEditPresenter submit post ok now validating");
                        fetchSnips();

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


        log.info("SnipEditPresenter onDelete: snip id "+id);
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if(!Constants.DEPLOY){
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter onDeleteSnip updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {
            AutoBean<SnipBean> actionBean = beanery.snipBean();
            actionBean.as().setAction("delete");
            actionBean.as().setId(id);
            String json = AutoBeanCodex.encode( actionBean).getPayload();

            log.info("SnipEditPresenter submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok now vaildate for dropdown
                        log.info("SnipEditPresenter onDeleteSnip  ok now validating");

                        fetchSnips();

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

    // submit <=> save
    @Override
    public void submitBean(AutoBean<SnipBean> bean) {

        // get snip data from EditorClient widget
        String title = view.getEditorClientWidget().getSnipData().get("title");
        String contentAsText = view.getEditorClientWidget().getSnipData().get("contentAsString");
        String contentAsHtml = view.getEditorClientWidget().getSnipData().get("contentAsHtml");
        String coreCat = view.getEditorClientWidget().getSnipData().get("coreCat");
        String subCat = view.getEditorClientWidget().getSnipData().get("subCat");

        log.info("SnipEditPresenter submitBean bean: "+title);

        if (title.isEmpty()) {
            Window.alert("A snip needs at least a title");
            return;
        }

        // put snip data into the bean
        bean.as().setTitle(title);
        bean.as().setCoreCat(coreCat);
        bean.as().setSubCat(subCat);
        bean.as().setContent(contentAsText);
        bean.as().setAuthor(controller.getCurrentUserBean().as().getName());
        bean.as().setAction("save");

        log.info("SnipEditPresenter submitBean bean : title : " +  bean.as().getTitle());

        // now submit to server
        log.info("SnipEditPresenter submit to server");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if(!Constants.DEPLOY){
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");

        try {

            String json = AutoBeanCodex.encode(bean).getPayload();
            log.info("SnipEditPresenter submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok now vaildate for dropdown
                        log.info("SnipEditPresenter submit post ok now validating");
                        fetchSnips();
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



    // this methoud used to validate snip drop down in edit view
    private void fetchSnips() {

        log.info("SnipEditPresenter getSnipDemoResult");

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        if(!Constants.DEPLOY) {
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipEditPresenter getSnipDemoResult  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean>  currentBean = beanery.snipBean();
        currentBean.as().setAction("getall");
        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    JsArray<JSOModel> data =
                            JSOModel.arrayFromJson(response.getText());
                    if(data.length() == 0 )  return;

                    jSonList = new ArrayList<JSOModel>();

                    for (int i = 0; i < data.length(); i++) {
                        jSonList.add(data.get(i));

                    }

                    List<AutoBean<SnipBean>> beans = new ArrayList<AutoBean<SnipBean>>();


                    for (int k = 0; k < jSonList.size(); k++) {
                        // used to index the incoming json array
                        String counter = ""+k;
                        AutoBean<SnipBean> bean = AutoBeanCodex.decode(beanery, SnipBean.class, jSonList.get(k).get(counter));

                        log.info("" + bean.as().toString());
                        beans.add(bean);
                    }
                    log.info("SnipEditPresenter onResponseReceived passing thru this many beans " + beans.size());
                    beans.clear();
                    // set snip combo in EditorClientWidget
                    view.getEditorClientWidget().setSnipComboBox(data);

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

    @Override
    public void onSaveButtonClicked() {

        log.info("SnipEditPresenter  onSaveButtonClicked BEGIN ");

    }

    @Override
    public void onCloseButtonClicked() {
        log.info("SnipEditPresenter  onCloseButtonClicked  ");

    }


    public SnipEditView getView() {
        return view;
    }


    @Override
    public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
        log.info("SnipEditPresenter  onValueChange" +stringValueChangeEvent.getValue());
        if(stringValueChangeEvent.getValue().equals("snips")) {

            if(!controller.getCurrentUserBean().as().isAuth() ) {
                History.newItem(RDLConstants.Tokens.WELCOME);
                History.fireCurrentHistoryState();

            }
        }
    }
}
