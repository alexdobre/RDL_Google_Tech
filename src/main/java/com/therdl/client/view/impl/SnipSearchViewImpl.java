package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SnipListRowWidget;
import com.therdl.client.view.widget.SnipSearchWidget;
import com.therdl.client.view.widget.WidgetHolder;
import com.therdl.client.view.widgetclosure.EditorListWidget;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SnipSearchViewImpl extends Composite implements SnipSearchView  {

    private static Logger log = Logger.getLogger("");

	private static SnipSearchViewImplUiBinder uiBinder = GWT
			.create(SnipSearchViewImplUiBinder.class);

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface SnipSearchViewImplUiBinder extends
			UiBinder<Widget, SnipSearchViewImpl> {
	}
	
	private Presenter presenter;
    @UiField AppMenu appMenu;
    @UiField Widget snipSearchWidget;
    @UiField
    FlowPanel snipListRow;
    private  AutoBean<CurrentUserBean> currentUserBean;
    private SnipListRowWidget snipListRowWidget;

    private EditorListWidget editorListWidget;

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean) {

        initWidget(uiBinder.createAndBindUi(this));
        log.info("SnipSearchViewImpl constructor");
        this.currentUserBean  =  currentUserBean;
        snipSearchWidget = new SnipSearchWidget(this);
        editorListWidget = (EditorListWidget) WidgetHolder.getInstance().getEditorListWidget();
	}

    /**
     *  shows closure editor list widget with the snip data
     */

    @Override
    public void getSnipListDemoResult(JsArray<JSOModel> snips) {
        log.info("SnipSearchViewImpl getSnipListDemoResult "+ snips.length());
        snipListRow.add(editorListWidget);
        editorListWidget.bootStrapList(editorListWidget, snips);
    }

    @Override
    public void updateListWidget(JsArray<JSOModel> snips){
        log.info("SnipSearchViewImpl updateListWidget "+ snips.length());
        editorListWidget.bootStrapList(editorListWidget, snips);
    }

    @Override
    public void setloginresult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true "+name );

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(name);
            this.appMenu.setLogInVisible(false);
        }

        else {
            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }

    }

    @Override
    public AppMenu getAppMenu() {
        return this.appMenu;
    }

    @Override
    public void searchSnips(String match) {
        presenter.searchSnips(match);
    }

}
