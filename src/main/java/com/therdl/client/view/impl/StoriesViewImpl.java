package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.LoadingWidget;
import com.therdl.client.view.widget.SearchFilterWidget;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * StoriesViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the RDL forum implementation
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ AppMenu appMenu the upper menu view
 * fields below are standard GWT UIBinder display elements
 * @ AutoBean<CurrentUserBean> currentUser  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * maintains client side state
 */
public class StoriesViewImpl extends Composite implements SearchView {

    private final static String MODULE_NAME = RDLConstants.Modules.STORIES;

    private static Logger log = Logger.getLogger("");

    private static StoriesViewImplUiBinder uiBinder = GWT.create(StoriesViewImplUiBinder.class);

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    interface StoriesViewImplUiBinder extends UiBinder<Widget, StoriesViewImpl> {
    }

    private Presenter presenter;

    SearchFilterWidget searchFilterWidget;

    @UiField
    AppMenu appMenu;


    @UiField
    FlowPanel threadSearchWidgetPanel;

    @UiField
    FlowPanel threadListRowContainer;

    @UiField
    LoadingWidget threadLoadingWidget;

    private AutoBean<CurrentUserBean> currentUserBean;

    private AutoBean<SnipBean> currentSearchOptionsBean;

    private Beanery beanery = GWT.create(Beanery.class);

    private String token;

    private String authorName;

    public StoriesViewImpl(AutoBean<CurrentUserBean> currentUserBean) {
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
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        searchFilterWidget = new SearchFilterWidget(this, authorName, MODULE_NAME);
        threadSearchWidgetPanel.add(searchFilterWidget);

        if (token.equals(RDLConstants.Tokens.STORIES)) {
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
        threadListRowContainer.clear();
        searchFilterWidget.removeFromParent();
    }

    public AutoBean<SnipBean> initSearchOptionsBean() {
        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
        searchOptionsBean.as().setAuthor(authorName);
        searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
        searchOptionsBean.as().setSortOrder(-1);
        searchOptionsBean.as().setSnipType(RDLConstants.SnipType.THREAD);

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
        threadListRowContainer.clear();
        threadListRowContainer.add(new ListWidget(this, beanList, pageIndex));
        threadLoadingWidget.getElement().getStyle().setProperty("display","none");
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
        threadLoadingWidget.getElement().getStyle().setProperty("display","block");
        currentSearchOptionsBean = searchOptionsBean;
        presenter.searchSnips(searchOptionsBean, pageIndex);
    }

    /**
     * call presenter function to retrieve initial list for snips
     */
    @Override
    public void getInitialSnipList(int pageIndex) {
        currentSearchOptionsBean = null;
        threadLoadingWidget.getElement().getStyle().setProperty("display","block");

        presenter.searchSnips(initSearchOptionsBean(), pageIndex);
    }
}
