package com.therdl.client.view.widget;

import java.util.Set;
import java.util.logging.Logger;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.therdl.client.dto.SnipProxy;

import com.therdl.client.view.widget.editor.SnipEditor;
import com.therdl.shared.beans.SnipBean;


/**
 * Responsible for showing and dismissing the SnipEditor. Wires it up to the
 * request factory and SnipProxy
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
    private AutoBean<SnipBean>  currentBean;

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

    public SnipEditorWorkflow() {
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
     * Called by the edit dialog's save button. This method will flush the
     * contents of the UI into the SnipProxy that is being edited, check for
     * errors, and send the request to the server.
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

    /**
     * Construct and display the UI that will be used to edit the current
     * PersonProxy, using the given RequestContext to accumulate the edits.
     */
    public void edit(RequestContext requestContext) {

        dialog.center();
    }

    public void submitBean(AutoBean<SnipBean> bean) {

        // if empty then created in Dynamic post box
        currentBean = bean;
        currentBean.as().setTitle(snipEditor.getTitle());
        currentBean.as().setContentAsString(snipEditor.getContentAsText());
        currentBean.as().setContentAsHtml(snipEditor.getContentAsHtml());
        currentBean.as().setAuthor("demo user");
        currentBean.as().setServerMessage("submit");
        currentBean.as().setAction("save");
        log.info("SnipEditorWorkflow submitBean bean : "+currentBean.as().toString());
        // now submit to server



    }


}
