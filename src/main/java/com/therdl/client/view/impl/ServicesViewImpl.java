package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.RegisterView;
import com.therdl.client.view.ServicesView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.*;

import java.util.logging.Logger;

/**
 *
 */
public class ServicesViewImpl extends Composite implements ServicesView {

    private static Logger log = Logger.getLogger("");
    private final  AutoBean<CurrentUserBean> currentUserBean;


    interface ServicesViewImplUiBinder extends UiBinder<Widget, ServicesViewImpl> {
    }

    private static ServicesViewImplUiBinder uiBinder = GWT.create(ServicesViewImplUiBinder.class);

    private ServicesView.Presenter presenter;

    private Beanery beanery = GWT.create(Beanery.class);

    @UiField
    HTMLPanel profile;

    @UiField
    AppMenu appMenu;


    public ServicesViewImpl(final AutoBean<CurrentUserBean> cUserBean) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUserBean  =  cUserBean;
         setAppMenu(currentUserBean);


        // user has just sucessfully logged in update app menu
        GuiEventBus.EVENT_BUS.addHandler(LogInOkEvent.TYPE, new LogInOkEventEventHandler()  {

            @Override
            public void onLogInOkEvent(LogInOkEvent onLoginOkEvent) {
                currentUserBean.as().setAuth(true);
                setAppMenu(currentUserBean);
            }
        });


    }

    @Override
    public void setAppMenu(AutoBean<CurrentUserBean> currentUserBean) {
        if (currentUserBean.as().isAuth()) {
            log.info("ServicesViewImpl setAppMenu auth true "+currentUserBean.as().getName() );
            profile.setVisible(true);
            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(currentUserBean.as().getName());
            this.appMenu.setEmail(currentUserBean.as().getEmail());
            this.appMenu.setLogInVisible(false);
        }

        else {
            profile.setVisible(false);
            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }

    }


    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }


}
