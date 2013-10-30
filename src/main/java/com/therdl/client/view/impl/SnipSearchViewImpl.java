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
 * SnipSearchViewImpl class is a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI where user can search snips
 *
 *  @ SnipSearchView.Presenter presenter the presenter for this view
 *  see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 *  @ Beanery beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 *  @ AppMenu appMenu the upper menu view
 *  @ SearchListWidget searchListWidget the list widget
 *  @ SearchFilterWidget searchFilterWidget widget for the search filter
 *
 *  fields below are standard GWT form fields for this view
 *  @ DockLayoutPanel snipEditDocPanel
 *  @ FlowPanel snipListRow
 *  @ FlowPanel snipSearchWidgetPanel
 */

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

    private String token;

    /**
     * SnipSearchViewImpl constructor
     * @param currentUserBean current logged user bean
     * @param token url token, in the bookmark search token contains search parameters with the following format
     * example http://localhost:8080/#snips:title=aaa:coreCat=Compatibility:author=serine
     */

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean, String token) {

        initWidget(uiBinder.createAndBindUi(this));
        log.info("SnipSearchViewImpl constructor token" + token);
        this.currentUserBean  =  currentUserBean;
        this.token = token;
        searchFilterWidget = new SearchFilterWidget(this);
        snipSearchWidgetPanel.add(searchFilterWidget);
        searchListWidget =  new SearchListWidget();
        snipSearchDocPanel.setSize("95%", "95%");
	}

    @Override
    protected void onLoad() {
        super.onLoad();
        if(token.equals(RDLConstants.Tokens.SNIPS))
            getInitialSnipList();
        else {
            doFilterSearch(parseToken());
        }

    }

    @Override
    public void showSnipList(JsArray<JSOModel> snips) {
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

    /**
     * call presenter function to search snips for the given search options
     * @param searchOptionsBean bean for the search options
     */

    @Override
    public void doFilterSearch(AutoBean<SnipBean> searchOptionsBean) {
        presenter.searchSnips(searchOptionsBean);
    }

    /**
     * call presenter function to retrieve initial list for snips
     */
    @Override
    public void getInitialSnipList() {
        presenter.getInitialSnipList();
    }

    /**
     * parses the token and creates searchOptionsBean bean object for search options
     * @return searchOptionsBean
     */
    private AutoBean<SnipBean> parseToken() {
        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
        String[] tokenSplit = token.split(":");
        for (int i=1; i<tokenSplit.length; i++) {
            String[] keyVal = tokenSplit[i].split("=");
            if(keyVal[0].equals(RDLConstants.BookmarkSearch.TITLE)) {
                searchOptionsBean.as().setTitle(keyVal[1].replace("+"," "));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.CORE_CAT)) {
                searchOptionsBean.as().setCoreCat(keyVal[1].replace("+"," "));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.SUB_CAT)) {
                searchOptionsBean.as().setSubCat(keyVal[1].replace("+"," "));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.POS_REF)) {
                searchOptionsBean.as().setPosRef(Integer.parseInt(keyVal[1]));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.NEUTRAL_REF)) {
                searchOptionsBean.as().setNeutralRef(Integer.parseInt(keyVal[1]));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.NEGATIVE_REF)) {
                searchOptionsBean.as().setNegativeRef(Integer.parseInt(keyVal[1]));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.REP)) {
                searchOptionsBean.as().setRep(Integer.parseInt(keyVal[1]));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.CONTENT)) {
                searchOptionsBean.as().setContent(keyVal[1].replace("+"," "));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.AUTHOR)) {
                searchOptionsBean.as().setAuthor(keyVal[1].replace("+"," "));
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.DATE_FROM)) {
                searchOptionsBean.as().setDateFrom(keyVal[1]);
            }

            if(keyVal[0].equals(RDLConstants.BookmarkSearch.DATE_TO)) {
                searchOptionsBean.as().setDateTo(keyVal[1]);
            }
        }

        return searchOptionsBean;
    }

}
