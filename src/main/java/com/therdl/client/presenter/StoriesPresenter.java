package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.StoriesView;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.logging.Logger;

/**
 * storiesPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will encapsulate the RDL forum presentation
 */
public class StoriesPresenter implements Presenter, StoriesView.Presenter{
    private static Logger log = Logger.getLogger("");

    private final StoriesView storiesView;

    private final AppController controller;


    public StoriesPresenter(StoriesView storiesView, AppController controller) {
        this.controller = controller;
        this.storiesView = storiesView;
        this.storiesView.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(storiesView.asWidget());
    }

    /**
     * standard runtime method for MVP architecture
     *
     * @param container       the view container
     * @param currentUserBean the user state bean, mainly used for authorisation
     *                        and to update the menu
     */

    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(storiesView.asWidget());
        if (!controller.getCurrentUserBean().as().isAuth()) {
            log.info("WelcomePresenter go !controller.getCurrentUserBean().as().isAuth()");
            storiesView.getAppMenu().setLogOutVisible(false);
            storiesView.getAppMenu().setSignUpVisible(true);
            storiesView.getAppMenu().setUserInfoVisible(false);
        }
    }



}
