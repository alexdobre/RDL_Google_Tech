package com.therdl.client.view.widgetclosure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.cssbundles.Resources;


public class EditorClientWidget extends Composite  {

	private static EditorClientViewUiBinder uiBinder = GWT
			.create(EditorClientViewUiBinder.class);


	interface EditorClientViewUiBinder extends
			UiBinder<Widget, com.therdl.client.view.widgetclosure.EditorClientWidget> {
	}

    boolean isInjected;


	public EditorClientWidget() {
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

    /*
     * JS native function which calls SnipEditor cinstructor function from the snipeditor.js
     */

    private native void bootStrapEditor(EditorClientWidget w ) /*-{
	    var snipEditor = new $wnd.widjdev.SnipEditor();
    }-*/;

    private native void resetDom() /*-{
        // removes dom objects, the container is the editorContainer
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
