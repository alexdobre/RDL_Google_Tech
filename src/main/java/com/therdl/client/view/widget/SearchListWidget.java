package com.therdl.client.view.widget;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.SnipViewEvent;

/**
 * This class manages the Closure ListWidget
 * see http://closuretools.blogspot.com/2010/07/introducing-closure-library-editor.html
 * the closure code is found at com.therdl.client.view.cssbundles/tabdev.js
 * the closure code is managed by JSNI methods
 * see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsJSNI.html
 * the JSOModel class is used extensively, JSOModel extends the GWT JavaScriptObject class,
 * A JavaScriptObject cannot be created directly. JavaScriptObject should
 * be declared as the return type of a JSNI method that returns native (non-Java) objects
 * JSOModel is designed to smooth out this at times abrupt delimination between javascript and java
 * ie Java String <-->JSOModel<--->JavaScriptObject<-->JSNI
 *
 * @ void setViewButtonCallbackGWT JSNI method  allows callback thru the java method viewButtonCallbackGWT
 * @ static void viewButtonCallbackGWT passes thru the setViewButtonCallbackGWT JSNI method open a snip
 * view event
 */
public class SearchListWidget extends Composite {


    private static SearchListViewUiBinder uiBinder = GWT.create(SearchListViewUiBinder.class);

    interface SearchListViewUiBinder extends UiBinder<Widget, SearchListWidget> {
    }

    public SearchListWidget() {

        Resources.INSTANCE.listCss().ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));
        setStyleName("listDisplay");
    }


    @Override
    protected void onLoad() {
        super.onLoad();
        injectScript();
        setViewButtonCallbackGWT(this);
        setEditButtonCallbackGWT(this);
        this.setVisible(true);
    }


    @Override
    protected void onUnload() {
        super.onUnload();
        resetDom();
        doDetachChildren();

    }

    /**
     * ensures com.therdl.client.view.cssbundles/tabdev.js is attached to the window js object when this
     * widget comes into focus
     */
    private void injectScript() {
        ScriptInjector.fromString(Resources.INSTANCE.widjdevList().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();
    }


    /**
     * destroys the Closure ListWidget when the ListWidget goes out of focus
     */
    private native void resetDom() /*-{
        var toolbars = $doc.getElementsByClassName('goog-toolbar');

        if (toolbars) {
            while (toolbars.length > 0) {
                toolbars[0].parentNode.removeChild(toolbars[0]);
            }

        }

        var popUps = $doc.getElementsByClassName('modal-dialog');

        if (popUps) {

            while (popUps.length > 0) {
                popUps[0].parentNode.removeChild(popUps[0]);
            }

        }

        $wnd.widjdev.tabdev = null;
    }-*/;


    /**
     * JSNI set up code. This will call js function, data is converted like this [{},{}]
     *
     * @param SearchListWidget  w === to 'this' in JSNI
     * @param JsArray<JSOModel> data
     * @param int               pSize paging variable
     */
    public native void bootStrapList(SearchListWidget w, JsArray<JSOModel> data, int pSize) /*-{
        var menu = $doc.getElementById('menu');
        // data now have the following format: [{"0":"{}"},{"1":"{}"}]: Change that like this [{},{}]
        var dataJs = [];
        for (var i = 0; i < data.length; i++) {
            dataJs.push(eval('(' + data[i][i] + ')'));
        }
        // calls closure js function from tabdev.js

        var tabCount = Math.ceil(dataJs.length / pSize);
        if (dataJs.length == 0) {
            pSize = 1;
            tabCount = 1;
        }
        $wnd.widjdev.tabdev.setTabs(menu, dataJs, pSize, tabCount);

    }-*/;


    /**
     * opens a snip view for a snip with a unique identifier for the snip
     * called form the closure code by the setViewButtonCallbackGWT(SearchListWidget x)
     * JSNI method
     *
     * @param String snipId unique identifier
     */
    public static void viewButtonCallbackGWT(String snipId) {
        // open a new snip view
        GuiEventBus.EVENT_BUS.fireEvent(new SnipViewEvent(snipId));
    }

    public static void editButtonCallbackGWT(String snipId) {
        History.newItem(RDLConstants.Tokens.SNIP_EDIT + ":" + snipId);
    }

    public native void setViewButtonCallbackGWT(SearchListWidget x)/*-{
        $wnd.viewButtonCallbackGWT = @com.therdl.client.view.widget.SearchListWidget::viewButtonCallbackGWT(*);
    }-*/;

    /**
     * JSNI method opens a snip view for a snip with a call to viewButtonCallbackGWT(String snipId)
     * called by Closure event in com.therdl.client.view.cssbundles/tabdev.js
     * JSNI method
     *
     * @param SearchListWidget x  SearchListWidget === 'this' in JSNI
     */
    public native void setEditButtonCallbackGWT(SearchListWidget x) /*-{
        $wnd.editButtonCallbackGWT = @com.therdl.client.view.widget.SearchListWidget::editButtonCallbackGWT(*);
    }-*/;
}
