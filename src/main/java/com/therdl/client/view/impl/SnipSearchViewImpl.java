package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.SnipSearchView;
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
    FlowPanel snipSearchWidgetPanel;

    @UiField
    FlowPanel snipListRowContainer;

    @UiField
    LoadingWidget loadingWidget;

    private AutoBean<CurrentUserBean> currentUserBean;
    private AutoBean<SnipBean> currentSearchOptionsBean;

    private SearchListWidget searchListWidget;
    private TabPanel tabPanel;

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

    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if (token.equals(RDLConstants.Tokens.SNIPS))
            getInitialSnipList(0);
        else {
            doFilterSearch(parseToken(), 0);
        }

    }

    @Override
    protected void onUnload() {
        super.onUnload();
        currentSearchOptionsBean = null;
        snipListRowContainer.clear();
    }

    @Override
    public void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
        snipListRowContainer.clear();

        // default size of rows in one tab
        int listRowSize = Constants.DEFAULT_PAGE_SIZE;
        int tabCount = 1;
        if(beanList.size() != 0) {
            tabCount = (int) Math.ceil((double)beanList.get(0).as().getCount()/listRowSize);
        }

        tabPanel = new TabPanel();

        // creates tabs of count tabCount
        for (int i=1; i<=tabCount; i++) {
            // creates content of current tab
            FlowPanel tabContent = new FlowPanel();
            if(beanList.size() == 0) {
                tabContent.add(new Label(RDL.i18n.noDataToDisplay()));
            }
            tabPanel.add(tabContent, i+"");

        }


        tabPanel.setHeight("100%");
        tabPanel.setWidth("100%");
        //select first tab

        for (int j=0; j<beanList.size(); j++) {
            SnipListRow snipListRow = new SnipListRow(beanList.get(j), currentUserBean, true);
            ((FlowPanel) tabPanel.getWidget(pageIndex)).add(snipListRow);
        }
        log.info("pageIndex="+pageIndex);
        tabPanel.selectTab(pageIndex);

        tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> integerBeforeSelectionEvent) {
                if(currentSearchOptionsBean != null) {
                    doFilterSearch(currentSearchOptionsBean, integerBeforeSelectionEvent.getItem());
                } else {
                    getInitialSnipList(integerBeforeSelectionEvent.getItem());
                }
            }
        });
        snipListRowContainer.add(tabPanel);
        loadingWidget.getElement().getStyle().setProperty("display","none");
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
        presenter.getInitialSnipList(pageIndex);
    }

    /**
     * parses the token and creates searchOptionsBean bean object for search options
     * @return searchOptionsBean
     */
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
