package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.WelcomeView;
import com.therdl.shared.Constants;
import com.therdl.shared.LoginHandler;
import com.therdl.shared.beans.*;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogInOkEvent;

import java.util.Date;
import java.util.logging.Logger;


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

public class WelcomePresenter implements Presenter, WelcomeView.Presenter {

    private static Logger log = Logger.getLogger("");
    private Beanery beanery = GWT.create(Beanery.class);

    private String avatarUrl = null;

    private final WelcomeView welcomeView;

    private final AppController controller;


    public WelcomePresenter(WelcomeView welcomeView, AppController controller) {
        this.controller = controller;
        this.welcomeView = welcomeView;
        this.welcomeView.setPresenter(this);

    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        welcomeView.init();
        container.add(welcomeView.asWidget());
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

        if (!controller.getCurrentUserBean().as().isAuth()) {
            log.info("WelcomePresenter go !controller.getCurrentUserBean().as().isAuth()");
            //check the cookie
            String sessionID = Cookies.getCookie("sid");
            if (sessionID != null) {
                //check if the SID is found and authenticate the user
            }


            welcomeView.getAppMenu().setLogOutVisible(false);
            welcomeView.getAppMenu().setSignUpVisible(true);
            welcomeView.getAppMenu().setUserInfoVisible(false);
        }
    }

    /**
     * calls com.therdl.server.restapi.SessionServlet class to authorise user from database, creates
     * a AutoBean<AuthUserBean> authBean from the users credentials and submits it as a json serialised object
     * calls AppController controller and  WelcomeView  welcomeView objects
     * controller.setCurrentUserBean(name, email, avatarUrl,  auth)::  sets the authorisation state based on
     * authorise user from database result
     * welcomeView.setloginresult(name, email, auth):: updatses the view with credentials mainly for the
     * upper menu
     *
     * @param emailTxt     String unique identifier for login and subsequent granted state information
     * @param passwordText String password identifier for login
     * @param loginHandler loginHandler which is called when login is successful
     */
    public void doLogIn(String emailTxt, String passwordText, Boolean rememberMe, String sid, final LoginHandler loginHandler) {

        log.info("v onSubmit password " + passwordText + " emailTxt  " + emailTxt);
        //used in inner class logic
        final Boolean innerRememberMe = rememberMe;

        String authUrl = GWT.getModuleBaseURL() + "getSession";

        if (!Constants.DEPLOY) {
            authUrl = authUrl.replaceAll("/therdl", "");
        }

        log.info("WelcomePresenter submit updateUrl: " + authUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(authUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {
            AutoBean<AuthUserBean> authBean = beanery.authBean();
            if (sid!=null && emailTxt == null){
                authBean.as().setSid(sid);
                authBean.as().setAction("sidAuth");
            }else{
                authBean.as().setPassword(passwordText);
                authBean.as().setEmail(emailTxt);
                authBean.as().setRememberMe(rememberMe);
                authBean.as().setAction("auth");
            }
            String json = AutoBeanCodex.encode(authBean).getPayload();

            log.info("WelcomePresenter submit json: " + json);
            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok move forward
                        log.info("WelcomePresenter onSubmit post ok");
                        log.info("WelcomePresenter onSubmit onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                        log.info("WelcomePresenter onSubmit onResponseReceived json" + response.getText());
                        JSOModel data = JSOModel.fromJson(response.getText());

                        AutoBean<AuthUserBean> authUserBean = AutoBeanCodex.decode(beanery, AuthUserBean.class, response.getText());

                        String email = data.get("email");
                        String name = data.get("name");
                        boolean isRDLSupporter = data.getBoolean("isRDLSupporter");

                        boolean auth = data.getBoolean("auth");
                        if (!auth) {
                            log.info("WelcomePresenter onResponseReceived  !auth  ");
                            // use app menu
                            welcomeView.showLoginFail();
                            controller.setCurrentUserBean("", "", avatarUrl, auth, null, isRDLSupporter);
                        } else {

                            if (data.get("name") != null) {
                                log.info("WelcomePresenter  avatarUrl exists" + data.get("avatarUrl"));
                                avatarUrl = data.get("avatarUrl");
                            }
                            controller.setCurrentUserBean(name, email, avatarUrl, auth, authUserBean.as().getTitles(), isRDLSupporter);
                            welcomeView.setLoginResult(name, email, auth);

                            //if there is no cookie and remember me was set we create a new cookie
                            if (innerRememberMe && Cookies.getCookie("sid") == null){
                                //set session cookie for 14 day expiry.
                                String sessionID = data.get("sid");
                                final long DURATION = 1000 * 60 * 60 * 24 * 14;
                                Date expires = new Date(System.currentTimeMillis() + DURATION);
                                Cookies.setCookie("sid", sessionID, expires, null, "/", false);
                            }

                            // use app menu
                            // try and update any open view
                            GuiEventBus.EVENT_BUS.fireEvent(new LogInOkEvent());

                            if(loginHandler != null) {
                                log.info("loginHandler != null");
                                loginHandler.onSuccess(controller.getCurrentUserBean());

                            }
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SignInViewImpl onSubmit onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }


    }


}
