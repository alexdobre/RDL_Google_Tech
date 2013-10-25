package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;

/**
 * This class manages the Closure Editor Widget
 * see http://closuretools.blogspot.com/2010/07/introducing-closure-library-editor.html
 * the closure code is found at com.therdl.client.view.cssbundles/snipeditor.js
 * the closure code is managed by JSNI methods
 * see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsJSNI.html
 * the JSOModel class is used extensively, JSOModel extends the GWT JavaScriptObject class,
 * A JavaScriptObject cannot be created directly. JavaScriptObject should
 * be declared as the return type of a JSNI method that returns native (non-Java) objects
 * JSOModel is designed to smooth out this at times abrupt delimination between javascript and java
 * ie Java String <-->JSOModel<--->JavaScriptObject<-->JSNI
 *
 * @SnipEditViewImpl snipEditView, the parent container for thi widget, allows callback thru the
 * SnipEditViewImpl's presenter class
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */

public class EditorClientWidget extends Composite  {
    private static Logger log = Logger.getLogger("");
    private Beanery beanery = GWT.create(Beanery.class);

	private static EditorClientViewUiBinder uiBinder = GWT
			.create(EditorClientViewUiBinder.class);


	interface EditorClientViewUiBinder extends
			UiBinder<Widget, EditorClientWidget> {
	}

    boolean isInjected;

    @UiField
    Button saveSnip;
    @UiField
    Button postSnip;
    @UiField
    Button deleteSnip;

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
      //  bootStrapEditor(this);
        this.setVisible(true);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        resetDom();
        doDetachChildren();

    }


    /**
     * ensures com.therdl.client.view.cssbundles/snipeditor.js is attached to the window js object when this
     * widget comes into focus
     */
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

        AutoBean<SnipBean> newBean = beanery.snipBean();
        if(snipData.get("currentSnipId").equals("")) {
            snipEditView.submitBean(newBean);
        } else {
            newBean.as().setId(snipData.get("currentSnipId"));
            snipEditView.submitEditBean(newBean);
        }

        History.newItem(RDLConstants.Tokens.SNIPS);


        // clearEditor();
    }

    @UiHandler("deleteSnip")
    void onDeleteSnip(ClickEvent event) {
        // test data
        JSOModel snipData = getSnipData();
        if(snipData.get("currentSnipId").equals("")) {
            Window.alert("there is no selected snip to delete");
        } else {
            snipEditView.onDeleteSnip(snipData.get("currentSnipId"));
            Window.alert("deleted");
        }
        History.newItem(RDLConstants.Tokens.SNIPS);
        //clearEditor();
    }

    /*
     * JS native function which calls SnipEditor constructor function from the snipeditor.js
     */

    public native void bootStrapEditor(EditorClientWidget w, JSOModel snipData) /*-{
	    var snipEditor = new $wnd.widjdev.SnipEditor(snipData);
    }-*/;

    public native void bootStrapEditor(EditorClientWidget w) /*-{
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
