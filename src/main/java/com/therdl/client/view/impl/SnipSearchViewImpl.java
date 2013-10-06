package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SearchFilterWidget;
import com.therdl.client.view.widget.SearchListWidget;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.*;

import java.util.logging.Logger;

public class SnipSearchViewImpl extends Composite implements SnipSearchView  {

    private static Logger log = Logger.getLogger("");

	private static SnipSearchViewImplUiBinder uiBinder = GWT.create(SnipSearchViewImplUiBinder.class);

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface SnipSearchViewImplUiBinder extends UiBinder<Widget, SnipSearchViewImpl> { }
	
	private Presenter presenter;

    SearchFilterWidget searchFilterWidget;

    @UiField
    AppMenu appMenu;

    @UiField
    DockLayoutPanel snipSearchDocPanel;

    @UiField
    FlowPanel snipSearchWidgetPanel;

    @UiField
    FlowPanel snipListRow;

    private  AutoBean<CurrentUserBean> currentUserBean;
    private  AutoBean<SnipBean> currentSearchOptionsBean;

    private SearchListWidget searchListWidget;

    private Beanery beanery = GWT.create(Beanery.class);

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean) {

        initWidget(uiBinder.createAndBindUi(this));
        log.info("SnipSearchViewImpl constructor");
        this.currentUserBean  =  currentUserBean;
        searchFilterWidget = new SearchFilterWidget(this);
        snipSearchWidgetPanel.add(searchFilterWidget);
        searchListWidget =  new SearchListWidget();
        snipSearchDocPanel.setSize("95%", "95%");
	}

    @Override
    protected void onLoad() {
        super.onLoad();
        getInitialSnipList();
    }

    @Override
    public void showSnipList(JsArray<JSOModel> snips) {
        log.info("SnipSearchViewImpl getSnipListDemoResult "+ snips.length());
        snipListRow.add(searchListWidget);
        searchListWidget.bootStrapList(searchListWidget, snips, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public void setloginresult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true "+name );

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(name);
            this.appMenu.setEmail(email);
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
    public void doFilterSearch(AutoBean<SnipBean> searchOptionsBean) {
        presenter.searchSnips(searchOptionsBean);
    }

    @Override
    public void getInitialSnipList() {
        log.info("getInitialSnipList");
        presenter.getInitialSnipList();
    }

}
