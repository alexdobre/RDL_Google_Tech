package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SnipView;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.Map;
import java.util.logging.Logger;

/**
 * SnipViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI so user can view a Snip edit, is brought into focus
 * fired from a Closure  new Religion
 *
 * @ SnipViewPresenter.Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ Map<String, String> currentSnipView, this is a store for the parameters
 * to be extracted from the client side snip data, these parameters should be
 * used to call the backend to retrieve the full snip for this view, under construction
 * @ AppMenu appMenu the upper menu view
 */
public class SnipViewImpl extends Composite implements SnipView {

    private static Logger log = Logger.getLogger("");

    private final AutoBean<CurrentUserBean> currentUserBean;

    interface SnipViewImpllUiBinder extends UiBinder<Widget, SnipViewImpl> {
    }

    private static SnipViewImpllUiBinder uiBinder = GWT.create(SnipViewImpllUiBinder.class);

    private SnipView.Presenter presenter;

    Map<String, String> currentSnipView;

    // uibinder varaibles

    @UiField
    AppMenu appMenu;

    public SnipViewImpl(AutoBean<CurrentUserBean> currentUserBean) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUserBean = currentUserBean;
        setAppMenu(currentUserBean);

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;

    }

    /**
     * Sets the upper header Menu to the correct state for supplied credentials
     * called from presenter
     *
     * @param AutoBean<CurrentUserBean> currentUserBean)
     */
    @Override
    public void setAppMenu(AutoBean<CurrentUserBean> currentUserBean) {

        if (currentUserBean.as().isAuth()) {
            log.info("ProfileViewImpl setAppMenu auth true " + currentUserBean.as().getName());

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(currentUserBean.as().getName());
            this.appMenu.setEmail(currentUserBean.as().getEmail());
            this.appMenu.setLogInVisible(false);
        } else {

            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }


    }
}
