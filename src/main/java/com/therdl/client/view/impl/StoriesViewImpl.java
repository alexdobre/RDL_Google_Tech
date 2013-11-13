package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.StoriesView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.logging.Logger;

/**
 * StoriesViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the landing page
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ AppMenu appMenu the upper menu view
 * fields below are standard GWT UIBinder display elements
 * @ AutoBean<CurrentUserBean> currentUser  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * maintains client side state
 */
public class StoriesViewImpl extends Composite implements StoriesView {
    private static Logger log = Logger.getLogger("");


    interface StoriesViewImplUiBinder extends UiBinder<Widget, StoriesViewImpl> {
    }

    private static StoriesViewImplUiBinder uiBinder = GWT.create(StoriesViewImplUiBinder.class);


    private StoriesView.Presenter presenter;

    private AutoBean<CurrentUserBean> currentUser;

    @UiField
    AppMenu appMenu;



    public StoriesViewImpl(AutoBean<CurrentUserBean> currentUser) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUser = currentUser;
        //  appMenu.setUserInfoVisible(false);
        appMenu.setLogOutVisible(false);
        appMenu.setMainGroupVisible(true);

        appMenu.setSignUpVisible(true);
    }


    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        log.info("WelcomeViewImpl setPresenter");
    }

    @Override
    public AppMenu getAppMenu() {
        return appMenu;
    }
}
