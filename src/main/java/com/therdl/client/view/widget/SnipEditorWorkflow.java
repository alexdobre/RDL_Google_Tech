package com.therdl.client.view.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.therdl.client.dto.SnipProxy;

import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.client.view.widget.editor.SnipEditor;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;


/**
 * Responsible for displaying the CRUD functions( Input, Output)
 *
 * this widget can be refactored to fit the strict MVP Pattern
 *
 * currently this widget is loosley coupled in the sense that it updates and validates itself on the database state
 *
 * independently of the main MVP model
 *
 */
public class SnipEditorWorkflow extends Composite {

    private static Logger log = Logger.getLogger("");

    interface Binder extends UiBinder<DialogBox, SnipEditorWorkflow> {
        Binder BINDER = GWT.create(Binder.class);
    }

    interface Driver extends RequestFactoryEditorDriver<SnipProxy, SnipEditor> {
    }

    private Driver editorDriver;
    private SnipProxy snipProxy;

    private Beanery beanery = GWT.create(Beanery.class);

    @UiField(provided = true)
    SnipEditor snipEditor = new SnipEditor();
    @UiField
    HTMLPanel contents;
    @UiField
    DialogBox dialog;
    @UiField
    Button save;
    @UiField
    Button cancel;
    SnipEditViewImpl view;

    public SnipEditorWorkflow(SnipEditViewImpl view) {
        this.view = view;
        initWidget(Binder.BINDER.createAndBindUi(this));
    }

    public SnipEditorWorkflow(SnipProxy snipProxy) {

        this.snipProxy = snipProxy;

        //snipEditor = new SnipEditor();
        initWidget(Binder.BINDER.createAndBindUi(this));
        contents.addDomHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                    onCancel(null);
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSave(null);
                }
            }
        }, KeyUpEvent.getType());
    }


    public void setContent(String s) {
        snipEditor.setContent(s);


    }

    public void setEditorTitle(String s) {
        snipEditor.setEditorTitle(s);


    }


    /**
     * Called by the cancel button when it is clicked. This method will just
     * tear down the UI and clear the state of the workflow.
     */
    @UiHandler("cancel")
    void onCancel(ClickEvent event) {
        dialog.hide();
    }

    /**
     *  just a check for now, maybe will use local storage
     */
    @UiHandler("save")
    void onSave(ClickEvent event) {

        // this could be for local storage

        String title = snipEditor.getTitle();
        String contentAsText = snipEditor.getContentAsText();
        String contentAsHtml = snipEditor.getContentAsHtml();
        log.info("SnipEditorWorkflow onSave title: " + title);
        log.info("SnipEditorWorkflow onSave content as Text : " + contentAsHtml);
        log.info("SnipEditorWorkflow onSave content as Html : " + contentAsHtml);

    }


    public void onDelete(String id) {

        log.info("SnipEditorWorkflow onDeleter");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";
        updateUrl = updateUrl.replaceAll("/therdl", "");

        log.info("SnipEditorWorkflow submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {
            AutoBean<SnipBean> actionBean = beanery.snipBean();
            actionBean.as().setAction("delete");
            actionBean.as().setId(id);
            String json = AutoBeanCodex.encode( actionBean).getPayload();

            log.info("SnipEditorWorkflow submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok move forward
                        log.info("SnipEditorWorkflow submit post ok now validating");
                        validateDropDown();

                    } else {
                        log.info("SnipEditorWorkflow submit post fail");

                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditorWorkflow submit onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }


    }




    // called by DynamicPostLisBox
    public void submitBean(AutoBean<SnipBean> bean) {
        log.info("SnipEditorWorkflow submitBean bean");
        bean.as().setTitle(snipEditor.getTitle());
        bean.as().setContentAsString(snipEditor.getContentAsText());
        bean.as().setContentAsHtml(snipEditor.getContentAsHtml());
        bean.as().setAuthor("demo user");
        bean.as().setServerMessage("submit");
        bean.as().setAction("save");
        log.info("SnipEditorWorkflow submitBean bean : " +  bean.as().getTitle());
        // now submit to server

        log.info("SnipEditorWorkflow submit to server");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";
        updateUrl = updateUrl.replaceAll("/therdl", "");

        log.info("SnipEditorWorkflow submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {

            String json = AutoBeanCodex.encode( bean).getPayload();
            log.info("SnipEditorWorkflow submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok move forward
                        log.info("SnipEditorWorkflow submit post ok now validating");
                        validateDropDown();

                    } else {
                        log.info("SnipEditorWorkflow submit post fail");

                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditorWorkflow submit onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }

    }

    // in the strict MVP pattern this code would be in the presenter, later can refactor
    // called by DynamicPostLisBox
    public void submitEditBean(AutoBean<SnipBean> bean) {
        log.info("SnipEditorWorkflow submitEditBean bean");

        bean.as().setTitle(snipEditor.getTitle());
        bean.as().setContentAsString(snipEditor.getContentAsText());
        bean.as().setContentAsHtml(snipEditor.getContentAsHtml());
        bean.as().setAuthor("demo user");
        bean.as().setServerMessage("submit");
        bean.as().setAction("update");
        log.info("SnipEditorWorkflow submitBean bean : " +  bean.as().getTitle());
        // now submit to server

        log.info("SnipEditorWorkflow submit to server");
        String updateUrl = GWT.getModuleBaseURL() + "getSnips";
        updateUrl = updateUrl.replaceAll("/therdl", "");

        log.info("SnipEditorWorkflow submit updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {

            String json = AutoBeanCodex.encode( bean).getPayload();
            log.info("SnipEditorWorkflow submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                       // ok move forward
                        log.info("SnipEditorWorkflow submit post ok now validating");
                        validateDropDown();

                    } else {
                        log.info("SnipEditorWorkflow submit post fail");

                            }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditorWorkflow submit onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }

    }

    // this is to ensure the validity of the views snip display
    // ie only dispay vaild beans
    public void validateDropDown() {



        log.info("SnipEditorWorkflow validateDropDown()");

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        updateUrl = updateUrl.replaceAll("/therdl", "");

        log.info("SnipEditorWorkflow validateDropDown( updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean>  currentBean = beanery.snipBean();
        currentBean.as().setAction("getall");
        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    log.info("SnipEditorWorkflow validateDropDown(  onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                    log.info("SnipEditorWorkflow validateDropDown( onResponseReceived json" + response.getText());

                    JsArray<JSOModel> data =
                            JSOModel.arrayFromJson(response.getText());
                    List<JSOModel>  jSonList = new ArrayList<JSOModel>();

                    for (int i = 0; i < data.length(); i++) {
                        jSonList.add(data.get(i));

                    }

                    List<AutoBean<SnipBean>> beans = new ArrayList<AutoBean<SnipBean>>();

                    log.info("SnipEditorWorkflow validateDropDown( onResponseReceived json" + jSonList.get(0).get("0"));

                    for (int k = 0; k < jSonList.size(); k++) {
                        String counter = ""+k;
                        log.info("SnipEditorWorkflow validateDropDown( onResponseReceived counter " + counter);
                        log.info("SnipEditorWorkflow validateDropDown( onResponseReceived jSonList.get(i).get(counter)" + jSonList.get(k).get(counter));
                        AutoBean<SnipBean> bean = AutoBeanCodex.decode(beanery, SnipBean.class, jSonList.get(k).get(counter));

                        log.info("" + bean.as().getTitle());
                        log.info("" + bean.as().getAuthor());
                        log.info("" + bean.as().getContentAsString());
                        log.info("" + bean.as().getTimeStamp());
                        beans.add(bean);
                    }

                    view.setSnipDropDown(beans);
                    beans.clear();

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditorWorkflow validateDropDown(  onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }




    }


}
