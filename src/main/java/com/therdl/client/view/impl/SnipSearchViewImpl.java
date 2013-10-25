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
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.*;

import java.util.logging.Logger;

/**
 * SnipSearchViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI so user can search and view a list of snips
 *
 * @ SearchFilterWidget searchFilterWidget, a filter widget for the search
 * @ SearchListWidget searchListWidget, this is the row widget in the list of rows
 * @ AppMenu appMenu the upper menu view
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ String token, a GWT History token see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html
 * @ AutoBean<CurrentUserBean> currentUserBean manages user state
 * @ AutoBean<SnipBean> currentSearchOptionsBean for autobeans see see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */
public class SnipSearchViewImpl extends Composite implements SnipSearchView {

    private static Logger log = Logger.getLogger("");

    private static SnipSearchViewImplUiBinder uiBinder = GWT.create(SnipSearchViewImplUiBinder.class);

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface SnipSearchViewImplUiBinder extends UiBinder<Widget, SnipSearchViewImpl> {
    }

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

    private AutoBean<CurrentUserBean> currentUserBean;
    private AutoBean<SnipBean> currentSearchOptionsBean;

    private SearchListWidget searchListWidget;

    private Beanery beanery = GWT.create(Beanery.class);

    private String token;

    public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean, String token) {

        initWidget(uiBinder.createAndBindUi(this));
        log.info("SnipSearchViewImpl constructor token" + token);
        this.currentUserBean = currentUserBean;
        this.token = token;
        searchFilterWidget = new SearchFilterWidget(this);
        snipSearchWidgetPanel.add(searchFilterWidget);
        searchListWidget = new SearchListWidget();
        snipSearchDocPanel.setSize("95%", "95%");
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (token.equals(RDLConstants.Tokens.SNIPS))
            getInitialSnipList();
        else {
            doFilterSearch(parseToken());
        }

    }

    @Override
    public void showSnipList(JsArray<JSOModel> snips) {
        log.info("SnipSearchViewImpl getSnipListDemoResult " + snips.length());
        snipListRow.add(searchListWidget);
        searchListWidget.bootStrapList(searchListWidget, snips, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public void setloginresult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true " + name);

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(name);
            this.appMenu.setEmail(email);
            this.appMenu.setLogInVisible(false);
        } else {
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

    private AutoBean<SnipBean> parseToken() {
        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
        String[] tokenSplit = token.split(":");
        for (int i = 1; i < tokenSplit.length; i++) {
            String[] keyVal = tokenSplit[i].split("=");
            if (keyVal[0].equals("title")) {
                searchOptionsBean.as().setTitle(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals("coreCat")) {
                searchOptionsBean.as().setCoreCat(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals("subCat")) {
                searchOptionsBean.as().setSubCat(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals("posRef")) {
                searchOptionsBean.as().setPosRef(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals("neutralRef")) {
                searchOptionsBean.as().setNeutralRef(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals("rep")) {
                searchOptionsBean.as().setRep(Integer.parseInt(keyVal[1]));
            }

            if (keyVal[0].equals("content")) {
                searchOptionsBean.as().setContent(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals("author")) {
                searchOptionsBean.as().setAuthor(keyVal[1].replace("+", " "));
            }

            if (keyVal[0].equals("dateFrom")) {
                searchOptionsBean.as().setDateFrom(keyVal[1]);
            }

            if (keyVal[0].equals("dateTo")) {
                searchOptionsBean.as().setDateTo(keyVal[1]);
            }
        }

        return searchOptionsBean;
    }

}
