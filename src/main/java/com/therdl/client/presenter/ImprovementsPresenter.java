package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.ImprovementsView;
import com.therdl.client.view.StoriesView;
import com.therdl.client.view.WelcomeView;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.logging.Logger;

/**
 * storiesPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will encapsulate the RDL user issues submission and voting
 */
public class ImprovementsPresenter extends RdlAbstractPresenter implements ImprovementsView.Presenter{


    private final ImprovementsView improvementsView;


    public ImprovementsPresenter(ImprovementsView improvementsView, AppController controller) {
        super(controller);
        this.improvementsView = improvementsView;
        this.improvementsView.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(improvementsView.asWidget());
        loginCookieCheck();
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
        container.add(improvementsView.asWidget());
        improvementsView.getAppMenu().setLogOutVisible(false);
        improvementsView.getAppMenu().setSignUpVisible(true);
        improvementsView.getAppMenu().setUserInfoVisible(false);

        loginCookieCheck();
    }



}
