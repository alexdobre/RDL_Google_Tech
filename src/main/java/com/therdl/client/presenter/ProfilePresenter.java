package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ProfileView;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.logging.Logger;

/**
 * ProfilePresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the profile related data in and out of the client
 * for example the User's Avatar upload and subsequent image presentation
 *
 *  @ ProfileView  profileView this presenter GUI component
 *  @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 *
 */
public class ProfilePresenter implements Presenter, ProfileView.Presenter {

    private static Logger log = Logger.getLogger("");
    private ProfileView profileView;
    private Beanery beanery = GWT.create(Beanery.class);


    public ProfilePresenter(ProfileView servicesView) {
        this.profileView = servicesView;
        servicesView.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(profileView.asWidget());
    }


    /**
     *  standard runtime method for MVP architecture
     * @param container  the view container
     * @param currentUserBean the user state bean, mainly used for authorisation
     * and to update the menu
     */
    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        container.clear();
        container.add(profileView.asWidget());
        profileView.setAppMenu(currentUserBean);
    }


}
