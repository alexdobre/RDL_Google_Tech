package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.client.view.widget.editor.SnipEditor;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;


/**
 * Responsible for displaying the CRUD functions( Input, Output)
 *
 * this widget can be refactored to fit the strict MVP Pattern
 *
 * currently this widget is loosely coupled in the sense that it updates and validates itself on the database state
 *
 * independently of the main MVP model
 *
 */
public class SnipEditorWorkflow extends Composite {

    private static Logger log = Logger.getLogger("");


    interface Binder extends UiBinder<DialogBox, SnipEditorWorkflow> {
        Binder BINDER = GWT.create(Binder.class);
    }


    private Beanery beanery = GWT.create(Beanery.class);



    @UiField(provided = true)
    SnipEditor snipEditor =  new SnipEditor();
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

    public SnipEditorWorkflow() {

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


    public SnipEditor getSnipEditor() {
        return snipEditor;
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
     *  just a check for now, maybe will use local storage , this bottom save in dialog pop up
     */
    @UiHandler("save")
    void onSave(ClickEvent event) {

        // this could be for local storage
        String title = snipEditor.getTitle();
        String contentAsText = snipEditor.getContentAsText();
        String contentAsHtml = snipEditor.getContentAsHtml();
        log.info("SnipEditorWorkflow onSave title: " + title);
//        log.info("SnipEditorWorkflow onSave content as Text : " + contentAsHtml);
        log.info("SnipEditorWorkflow onSave content as Html : " + contentAsHtml);
    }

     // save
    public void submitBean(AutoBean<SnipBean> bean) {
        log.info("SnipEditorWorkflow submitBean : " + bean.toString());
        view.submitBean(bean);
    }

    // update
    public void submitEditBean(AutoBean<SnipBean> bean) {
        log.info("SnipEditorWorkflow submitEditBean : " + bean.toString());
        view.submitEditBean(bean);
    }

    // delete
    public void onDelete(String id) {
        log.info("SnipEditorWorkflow onDelete : id =  " + id);
        view.onDeleteSnip(id);
    }



    public void createPost() {
        log.info("SnipEditorWorkflow createPost" );
        clearEditor();
        snipEditor.setContent("Edit the tilte and content then submit");

    }

    public void clearEditor() {
        log.info("SnipEditorWorkflow clearEditor " );
        snipEditor.setEditorTitle("");
        snipEditor.setContent("");
    }

}
