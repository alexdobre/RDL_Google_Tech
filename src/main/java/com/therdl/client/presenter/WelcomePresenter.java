package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.WelcomeView;
import com.therdl.shared.beans.CurrentUserBean;


/**
 * WelcomePresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 *
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ WelcomeView  welcomeView this presenter GUI component
 * @ String avatarUrl a URI to locate the user image on the filesystem or in the database
 * @ void doLogIn( String emailtxt, String passwordText ) user login to <URI base path>/getSession
 * calls com.therdl.server.restapi.SessionServlet class and updates the view depending on given/allowed
 * authorisation in the server callback method  onResponseReceived(Request request, Response response)
 */

public class WelcomePresenter extends RdlAbstractPresenter implements WelcomeView.Presenter {


    private String avatarUrl = null;

    private final WelcomeView welcomeView;


    public WelcomePresenter(WelcomeView welcomeView, AppController controller) {
        super(controller);
        this.welcomeView = welcomeView;
        this.welcomeView.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        welcomeView.init();
        container.add(welcomeView.asWidget());

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
        welcomeView.init();
        container.add(welcomeView.asWidget());
        welcomeView.getAppMenu().setLogOutVisible(false);
        welcomeView.getAppMenu().setSignUpVisible(true);
        welcomeView.getAppMenu().setUserInfoVisible(false);

        loginCookieCheck();
    }

}
