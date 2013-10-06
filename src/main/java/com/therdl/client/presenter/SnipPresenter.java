package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.widget.SnipView;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.Map;
import java.util.logging.Logger;

/**
 *  see snip view
 */
public class SnipPresenter implements Presenter, SnipView.Presenter {

    private static Logger log = Logger.getLogger("");

    private SnipView snipView;
    private Beanery beanery = GWT.create(Beanery.class);
    private Map<String, String> currentSnipView;
    private  AutoBean<CurrentUserBean> currentUserBean;

    public SnipPresenter(SnipView snipView, Map<String, String> currentSnipView, AutoBean<CurrentUserBean> currentUserBean) {
        this.snipView = snipView;
        this.currentSnipView = currentSnipView;
        this.currentUserBean = currentUserBean;
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(snipView.asWidget());

    }

    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(snipView.asWidget());
        snipView.setAppMenu(currentUserBean);
    }
}
