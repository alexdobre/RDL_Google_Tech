package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SearchListWidget;
import com.therdl.client.view.widget.SnipSearchWidget;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SearchOptionsBean;

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

    // paging parameter
    int pSize = 5;


    SnipSearchWidget snipSearchWidget;

    @UiField
    AppMenu appMenu;

    @UiField
    FlowPanel snipSearchWidgetPanel;

    @UiField
    FlowPanel snipListRow;

    private  AutoBean<CurrentUserBean> currentUserBean;
    private  AutoBean<SearchOptionsBean> currentSearchOptionsBean;

    private SearchListWidget searcListWidget;

    private Beanery beanery = GWT.create(Beanery.class);

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean) {

        initWidget(uiBinder.createAndBindUi(this));
        log.info("SnipSearchViewImpl constructor");
        this.currentUserBean  =  currentUserBean;
        snipSearchWidget = new SnipSearchWidget(this);
        snipSearchWidgetPanel.add(snipSearchWidget);
        searcListWidget =  new SearchListWidget();
	}

    @Override
    public void getSnipListDemoResult(JsArray<JSOModel> snips) {
        log.info("SnipSearchViewImpl getSnipListDemoResult "+ snips.length());
        snipListRow.add(searcListWidget);
        searcListWidget.bootStrapList(searcListWidget, snips, pSize);
    }

    @Override
    public void updateListWidget(JsArray<JSOModel> snips, int pSize){
        log.info("SnipSearchViewImpl updateListWidget "+ snips.length());
        snipListRow.add(searcListWidget);
        searcListWidget.bootStrapList(searcListWidget, snips, pSize);
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
        log.info("SnipSearchViewImpl searchSnips " + match+" : "+ pSize);

        if(currentSearchOptionsBean == null && match.equals("")) {
            Window.alert("there is no any search option");
            return;
        }

        if(currentSearchOptionsBean != null) {
            currentSearchOptionsBean.as().setTitle(match);
        } else {
            currentSearchOptionsBean = beanery.searchOptionsBean();
            currentSearchOptionsBean.as().setTitle(match);
            currentSearchOptionsBean.as().setPageSize(Constants.DEFAULT_PAGE_SIZE);
        }

        presenter.searchSnips(currentSearchOptionsBean);
        currentSearchOptionsBean = null;
    }

    @Override
    public void doFilterSearch(AutoBean<SearchOptionsBean> searchOptionsBean) {
        currentSearchOptionsBean = searchOptionsBean;
    }

}
