package com.therdl.client.view.widgetclosure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;


public class EditorClientWidget extends Composite  {
    private static Logger log = Logger.getLogger("");
    private Beanery beanery = GWT.create(Beanery.class);

	private static EditorClientViewUiBinder uiBinder = GWT
			.create(EditorClientViewUiBinder.class);


	interface EditorClientViewUiBinder extends
			UiBinder<Widget, com.therdl.client.view.widgetclosure.EditorClientWidget> {
	}

    boolean isInjected;

    @UiField
    Button saveSnip;
    @UiField
    Button postSnip;
    SnipEditViewImpl snipEditView;

	public EditorClientWidget(SnipEditViewImpl snipEditView) {
        this.snipEditView = snipEditView;
        Resources.INSTANCE.editorCss().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        injectScript();
        bootStrapEditor(this);
        this.setVisible(true);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        resetDom();
        doDetachChildren();

    }

    private void injectScript() {
        // uncomment condition because when switching list and editor views second time the js scripts does not included into the html
        //if(!isInjected) {
            ScriptInjector.fromString(Resources.INSTANCE.dialogView().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();
            isInjected = true;
        //}
    }

    @UiHandler("saveSnip")
    void onSaveSnip(ClickEvent event) {

        // test data
        JSOModel snipData = getSnipData();

        log.info("snipContent="+snipData.get("contentAsHtml"));
        log.info("snipTitle="+snipData.get("title"));
        log.info("coreCat="+snipData.get("coreCat"));
        log.info("subCat="+snipData.get("subCat"));
        log.info("currentSnipId="+snipData.get("currentSnipId"));

        AutoBean<SnipBean> newBean = beanery.snipBean();
        if(snipData.get("currentSnipId").equals("")) {
            snipEditView.submitBean(newBean);
            Window.alert("saved");
        } else {
            newBean.as().setId(snipData.get("currentSnipId"));
            snipEditView.submitEditBean(newBean);
            Window.alert("edited");
        }

        clearEditor();
    }

    /*
     * JS native function which calls SnipEditor constructor function from the snipeditor.js
     */

    private native void bootStrapEditor(EditorClientWidget w) /*-{
	    var snipEditor = new $wnd.widjdev.SnipEditor();
    }-*/;

    public native void setSnipComboBox(JsArray<JSOModel> data) /*-{
        // clear snip combo to init with new data
        var snipComboParent  = $doc.getElementById('snipComboParent');
        if(snipComboParent) {
            while (snipComboParent.lastChild)
                snipComboParent.removeChild(snipComboParent.lastChild);
        }
        // this will call a function from snipeditor.js which will create a combo box with snip titles
        $wnd.widjdev.SnipEditor.setSnipComboBox(data);
    }-*/;

    private native void clearEditor() /*-{
        $wnd.widjdev.SnipEditor.clearEditor();
    }-*/;

    private native void resetDom() /*-{
        // removes dom objects, the container is the editorContainer declared in the EditorClientWidget.ui.xml
        var editorContainer  = $doc.getElementById('editorContainer');
        if(editorContainer) {
            while (editorContainer.lastChild)
                editorContainer.removeChild(editorContainer.lastChild);
        }
        $wnd.widjdev.SnipEditor = null;
    }-*/;


    public void btnGetContentClick() {
        String contents = getContent();

        Window.alert(contents);

    }
    public void btnSetContentClick() {
        String temp = "this is the test content";
        setContent(temp);
        Window.alert("Set Content");
    }

    private native void getEditor() /*-{
        $wnd.document.getElementById('editorDiv').display= 'block';
    }-*/;

    /*
     * JS native function for calling js function which returns editor's input data
     * @return JSOModel for snip data
     */
    public native JSOModel getSnipData() /*-{
         return $wnd.widjdev.SnipEditor.getSnipData();
    }-*/;

    private native String getContent() /*-{
        return $wnd.widjdev.setEditor.getcontents();
    }-*/;

    private native void setContent(String t) /*-{
       $wnd.widjdev.setEditort.setcontents(t);
    }-*/;


    private native void hideEditor() /*-{
        $wnd.document.getElementById('editorDiv').display= 'none';
    }-*/;


}
