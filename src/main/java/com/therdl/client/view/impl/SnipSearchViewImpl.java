package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.*;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.*;

import java.util.ArrayList;
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
public class SnipSearchViewImpl extends Composite implements SearchView {

    private final static String MODULE_NAME = RDLConstants.Modules.IDEAS;

    private static Logger log = Logger.getLogger("");

    private static SnipSearchViewImplUiBinder uiBinder = GWT.create(SnipSearchViewImplUiBinder.class);

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    interface SnipSearchViewImplUiBinder extends UiBinder<Widget, SnipSearchViewImpl> {
    }

    private Presenter presenter;

    SearchFilterWidget searchFilterWidget;

    @UiField
    AppMenu appMenu;


    @UiField
    FlowPanel snipSearchWidgetPanel;

    @UiField
    FlowPanel snipListRowContainer;

    @UiField
    LoadingWidget loadingWidget;

    private AutoBean<CurrentUserBean> currentUserBean;

    private AutoBean<SnipBean> currentSearchOptionsBean;

    private Beanery beanery = GWT.create(Beanery.class);

    private String token;

    private String authorName;

    public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUserBean = currentUserBean;
    }

    public void setToken(String token) {

        String[] tokenSplit = token.split(":");
        if(tokenSplit.length == 2) {
            this.token = tokenSplit[0];
            this.authorName = tokenSplit[1];
        } else {
            this.token = token;
        }
        appMenu.setIdeasActive();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        searchFilterWidget = new SearchFilterWidget(this, authorName, MODULE_NAME);
        snipSearchWidgetPanel.add(searchFilterWidget);

        if (token.equals(RDLConstants.Tokens.SNIPS)) {
            if(authorName != null) {
                AutoBean<SnipBean> searchOptionsBean = initSearchOptionsBean();
                searchOptionsBean.as().setAuthor(authorName);

                doFilterSearch(searchOptionsBean, 0);
            } else {
                getInitialSnipList(0);
            }
        } else {
            doFilterSearch(ViewUtils.parseToken(beanery, token), 0);
        }

    }

    @Override
    protected void onUnload() {
        super.onUnload();
        currentSearchOptionsBean = null;
        snipListRowContainer.clear();
        searchFilterWidget.removeFromParent();
    }

    public AutoBean<SnipBean> initSearchOptionsBean() {
        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
        searchOptionsBean.as().setAuthor(authorName);
        searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
        searchOptionsBean.as().setSortOrder(-1);
        searchOptionsBean.as().setSnipType(searchFilterWidget.getCheckedSnipTypes());

        return searchOptionsBean;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public AutoBean<CurrentUserBean> getCurrentUserBean() {
        return currentUserBean;
    }

    public AutoBean<SnipBean> getCurrentSearchOptionsBean() {
        return currentSearchOptionsBean;
    }

    @Override
    public void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
        snipListRowContainer.clear();
        snipListRowContainer.add(new ListWidget(this, beanList, pageIndex));
        loadingWidget.getElement().getStyle().setProperty("display","none");
    }

    @Override
    public void setLoginResult(String name, String email, boolean auth) {
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

    /**
     * call presenter function to search snips for the given search options
     * @param searchOptionsBean bean for the search options
     * @param pageIndex
     */

    @Override
    public void doFilterSearch(AutoBean<SnipBean> searchOptionsBean, int pageIndex) {
        loadingWidget.getElement().getStyle().setProperty("display","block");
        currentSearchOptionsBean = searchOptionsBean;
        presenter.searchSnips(searchOptionsBean, pageIndex);
    }

    /**
     * call presenter function to retrieve initial list for snips
     */
    @Override
    public void getInitialSnipList(int pageIndex) {
        currentSearchOptionsBean = null;
        loadingWidget.getElement().getStyle().setProperty("display","block");

        presenter.searchSnips(initSearchOptionsBean(), pageIndex);
    }
}
