package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ServicesView;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.logging.Logger;

/**
 * For now will be profile related stuff
 * User Avatar upload
 */
public class ServicesPresenter implements Presenter, ServicesView.Presenter {

    private static Logger log = Logger.getLogger("");
    private ServicesView servicesView;
    private Beanery beanery = GWT.create(Beanery.class);


    public ServicesPresenter(ServicesView servicesView) {
        this.servicesView = servicesView;
        servicesView.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(servicesView.asWidget());
    }

    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(servicesView.asWidget());
        servicesView.setAppMenu(currentUserBean);
    }


}
