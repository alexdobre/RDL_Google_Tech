package com.therdl.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.presenter.*;

import com.therdl.client.view.*;
import com.therdl.client.view.impl.*;
import com.therdl.client.view.SnipView;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * While the overall application follows the Model View Presenter(MVP) design pattern
 * and the particular schema for MVP here known as Activities and Places
 * see http://www.gwtproject.org/doc/latest/DevGuideMvpActivitiesAndPlaces.html
 * this can be simply expressed as
 * 1. place --> view and
 * 2. activity --> user state/interaction
 * this class manages both Places and Activities and acts as a Controller class in the
 * client/view layer
 * <p/>
 * This class controls what view is presented and when it is presented. A particular
 * view is presented during user interaction principally based on  the state of 2 principal actors
 *
 * @ RDLConstants.Tokens.<history Token Type> identifies the view to be presented
 * @ AutoBean<Auth Type>   determines the authorisation state of the view presented
 * <p/>
 * it has utility methods and objects
 * @ Beanery beanery, the bean factory
 * @ <XX>View client side views generally UiBinder GUI classes
 * <p/>
 * important methods
 * @ bind() manages the client side event handlers for History and authorisation state client
 * side events
 * @ onValueChange(ValueChangeEvent<String> event) ValueChangeEvents are fired when the view is changed by a user
 * see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html
 * for any  ValueChangeEvent think (History event) the type of view is found, the presenter and view are
 * created and by examining the authorisation status of the user put in focus for the user if user is authorised
 * with the correct paramaters (eg menu options) for that users given  authorisation status (eg logged in)
 */
public class AppController implements Presenter, ValueChangeHandler<String> {

    private static Logger log = Logger.getLogger("");

    private HasWidgets container;

    private Beanery beanery = GWT.create(Beanery.class);

    /**
     * Current authentication rules are anyone can view but only registered user can edit
     * separate authorisation from user state with 2 user beans, current user and authorised user
     * for example auth user bean will have initial password for sign up.
     * The current user bean as a persistent client side bean will/must not contain password info
     * and any time for client side security
     */

    private AutoBean<AuthUserBean> authnBean = beanery.authBean();
    private AutoBean<CurrentUserBean> currentUserBean = beanery.currentUserBean();

    private WelcomeView welcomeView;
    private SnipEditView snipEditView;
    private SnipSearchView snipSearchView;
    private StoriesView storiesView;
    private ImprovementsView improvementsView;
    private RegisterView registerView;
    private ProfileView profileView;
    private SnipView snipView;

    public AppController() {

        bind();
    }

    /**
     * Binds the event handler instances to their specific events
     *
     * @ LogOutEvent  for user log out flow
     * @ SnipViewEvent this event creates a new SnipView, is noteworthy as it is fired after
     * a JSNI callback from the Closure SnipListWidget code
     */
    private void bind() {

        History.addValueChangeHandler(this);

        log.info("AppController bind() addValueChangeHandler");

        // logout event handler
        GuiEventBus.EVENT_BUS.addHandler(LogOutEvent.TYPE, new LogOutEventEventHandler() {
            @Override
            public void onLogOutEvent(LogOutEvent onLogOutEvent) {
                currentUserBean = Validation.resetCurrentUserBeanFields(currentUserBean);
                History.newItem(RDLConstants.Tokens.LOG_OUT);
                History.fireCurrentHistoryState();
            }
        });

        // SnipView event handler
        GuiEventBus.EVENT_BUS.addHandler(SnipViewEvent.TYPE, new SnipViewEventHandler() {

            @Override
            public void onSnipSelectEvent(SnipViewEvent event) {
                History.newItem(RDLConstants.Tokens.SNIP_VIEW+":"+event.getSnipId());
                //  History.fireCurrentHistoryState();
            }
        });


    }

    /**
     * present the Welcome landing page for a unAuthorised user
     *
     * @param container the Presenter View
     */

    @Override
    public void go(final HasWidgets container) {
        this.container = container;
        this.currentUserBean.as().setAuth(false);
        this.currentUserBean.as().setRegistered(false);
        if ("".equals(History.getToken())) {
            History.newItem(RDLConstants.Tokens.WELCOME);
        } else {
            History.fireCurrentHistoryState();
        }
    }

    /**
     * present the Welcome landing page for a user with a  authorised state
     *
     * @param HasWidgets container      the Presenter View
     * @param AutoBean   currentUserBean  a bean with  authorised state information(eg logged in or out)
     */
    @Override
    public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
        this.container = container;
        this.currentUserBean.as().setAuth(false);
        this.currentUserBean.as().setRegistered(false);
        if ("".equals(History.getToken())) {
            History.newItem(RDLConstants.Tokens.WELCOME);
        } else {
            History.fireCurrentHistoryState();
        }
    }

    /**
     * This binds the history tokens with the different application states
     *
     * @param ValueChangeEvent event   ValueChangeEvent  see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html
     *                         for ValueChangeEvent think HistoryEvent
     *                         String tokens are extracted from the HistoryEvents, this allows the correct view to be constructed and presented
     *                         this method provides the machinery
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        log.info("AppController onValueChange token before split is  " + token);
        String[] tokenSplit = token.split(":");

        if (tokenSplit.length != 0) {
            token = tokenSplit[0];
        }

        log.info("AppController onValueChange token is  " + token);

        if (token != null) {

            //***************************************WELCOME****************************
            if (token.equals(RDLConstants.Tokens.WELCOME)) {
                log.info("AppController Tokens.WELCOME");
                if (welcomeView == null) {
                    welcomeView = new WelcomeViewImpl(currentUserBean);
                }
                final WelcomePresenter welcomePresenter = new WelcomePresenter(welcomeView, this);
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {

                        welcomePresenter.go(container, currentUserBean);

                        if (currentUserBean.as().isAuth()) {
                            welcomeView.setloginresult(currentUserBean.as().getName(), currentUserBean.as().getEmail(), true);
                        }
                    }
                });
            }


            //***************************************SNIP_View****************************
            else if (token.equals(RDLConstants.Tokens.SNIP_VIEW)) {
                log.info("AppController Tokens.SNIP_VIEW");

                String currentSnipId = "";
                if (tokenSplit.length == 2) {
                    currentSnipId = tokenSplit[1];
                }

                if (snipView == null) {
                    snipView = new SnipViewImpl(currentUserBean);
                }

                final SnipPresenter snipPresenter = new SnipPresenter(snipView, currentSnipId, this);

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                        log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.SNIPS);
                    }

                    public void onSuccess() {

                        snipPresenter.go(container, currentUserBean);

                    }
                });

            }// end else

            //***************************************SNIPS****************************
            else if (token.equals(RDLConstants.Tokens.SNIPS)) {
                log.info("AppController Tokens.SNIPS");

                if (snipSearchView == null) {
                    snipSearchView = new SnipSearchViewImpl(currentUserBean, event.getValue());
                }

                final SnipSearchPresenter snipSearchPresenter = new SnipSearchPresenter(snipSearchView, this);

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                        log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.SNIPS);
                    }

                    public void onSuccess() {

                        snipSearchPresenter.go(container, currentUserBean);

                    }
                });

            }// end else

            //***************************************STORIES****************************
            else if (token.equals(RDLConstants.Tokens.STORIES)) {
                log.info("AppController Tokens.STORIES");

                if (storiesView == null) {
                    storiesView = new StoriesViewImpl(currentUserBean);
                }
                final StoriesPresenter storiesPresenter = new StoriesPresenter(storiesView, this);

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                        log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
                    }

                    public void onSuccess() {

                        storiesPresenter.go(container, currentUserBean);

                    }
                });

            }// end else

            //***************************************IMPROVEMENTS****************************
            else if (token.equals(RDLConstants.Tokens.IMPROVEMENTS)) {
                log.info("AppController Tokens.IMPROVEMENTS");

                if (improvementsView == null) {
                    improvementsView = new ImprovementsViewImpl(currentUserBean);
                }
                final ImprovementsPresenter improvementsPresenter = new ImprovementsPresenter(improvementsView, this);

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                        log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
                    }

                    public void onSuccess() {

                        improvementsPresenter.go(container, currentUserBean);

                    }
                });

            }// end else

            //***************************************SNIP_EDIT****************************
            // only authorised users
            else if (token.equals(RDLConstants.Tokens.SNIP_EDIT)) {

                String currentSnipId = "";
                if (tokenSplit.length == 2) {
                    currentSnipId = tokenSplit[1];
                }

                log.info("AppController Tokens.SNIP_EDIT token=" + token + ";currentSnipId=" + currentSnipId);


                if (snipEditView == null) {
                    snipEditView = new SnipEditViewImpl(currentUserBean);
                }
                final SnipEditPresenter snipEditPresenter = new SnipEditPresenter(snipEditView, currentSnipId, this);


                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        if (currentUserBean.as().isAuth()) {
                            snipEditPresenter.go(container, currentUserBean);
                        } else {
                            History.newItem(RDLConstants.Tokens.WELCOME);
                        }
                    }
                });
            }

            //*************************************** SIGN_UP ****************************
            else if (token.equals(RDLConstants.Tokens.SIGN_UP)) {
                currentUserBean = Validation.resetCurrentUserBeanFields(currentUserBean);
                if (registerView == null) {
                    registerView = new RegisterViewImpl();

                }

                final RegisterPresenter registerPresenter = new RegisterPresenter(registerView, this);
                log.info("AppController Tokens.SIGN_UP ");
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        registerPresenter.go(container, currentUserBean);

                    }
                });
            }

            //*************************************** PROFILE ****************************
            else if (token.equals(RDLConstants.Tokens.PROFILE)) {

                if (profileView == null) {
                    log.info("AppController profileView == null ");
                    profileView = new ProfileViewImpl(currentUserBean);

                } else {
                    log.info("AppController profileView == null else ");
                    profileView.setAvatarWhenViewIsNotNull();
                }

                final ProfilePresenter profilePresenter = new ProfilePresenter(profileView);
                log.info("AppController Tokens.SERVICES ");
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        profilePresenter.go(container, currentUserBean);

                    }
                });
            }


            //*************************************** LOG_OUT ****************************
            else if (token.equals(RDLConstants.Tokens.LOG_OUT)) {

                final WelcomePresenter welcomePresenter = new WelcomePresenter(welcomeView, this);
                log.info("AppController Tokens.LOG_OUT ");
                if (welcomeView != null) {

                    welcomeView.logout();
                }
                if (snipSearchView != null) {
                    snipSearchView.setloginresult(" ", " ", false);
                }

                if (snipEditView != null) {
                    snipEditView.setloginresult(" ", " ", false);
                }

                if (registerView != null) {
                    registerView.setloginresult(" ", " ", false);
                }

                if (profileView == null) {
                    profileView.setAppMenu(currentUserBean);

                }


                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        currentUserBean.as().setAuth(false);
                        if (welcomeView == null) {
                            welcomeView = new WelcomeViewImpl(currentUserBean);

                        }
                        currentUserBean.as().setAuth(false);
                        welcomePresenter.go(container, currentUserBean);
                        welcomeView.logout();
                    }
                });
            }


            //*************************************** PROFILE-Caching url ****************************
            else {

                String[] temp = token.split(":");

                log.info("AppController else parsing profile caching hash " + temp.length);

                if (temp[0].equals(RDLConstants.Tokens.PROFILE)) {

                    if (profileView == null) {
                        log.info("AppController profileView == null ");
                        profileView = new ProfileViewImpl(currentUserBean);

                    } else {
                        log.info("AppController profileView == null else ");
                        profileView.setAvatarWhenViewIsNotNull();
                    }

                    final ProfilePresenter profilePresenter = new ProfilePresenter(profileView);
                    log.info("AppController Tokens.SERVICES ");
                    GWT.runAsync(new RunAsyncCallback() {
                        public void onFailure(Throwable caught) {
                        }

                        public void onSuccess() {
                            profilePresenter.go(container, currentUserBean);

                        }
                    });

                }


            } // end else


        } // end  if token != null

    }// end onValueChanged

    public AutoBean<CurrentUserBean> getCurrentUserBean() {
        return currentUserBean;
    }


    /**
     * sets the currentUserBean for the  view in the WelcomePresenter dologin method
     * the values below are all strings extracted from a simple form input
     *
     * @param name      String  for user name
     * @param email     String  for user email
     * @param avatarUrl String for user image location acts as a url for mongo/filesystem
     * @param state     booleam for state information
     */
    public void setCurrentUserBean(String name, String email, String avatarUrl, boolean state) {
        this.currentUserBean.as().setAuth(state);
        this.currentUserBean.as().setName(name);
        this.currentUserBean.as().setEmail(email);
        this.currentUserBean.as().setAvatarUrl(avatarUrl);
    }


    /**
     * set in the RegisterPresenter onResponseReceived for  the server callback 'submitNewUser'
     * the values below are all strings extracted from a simple form input
     *
     * @param name  String for user name
     * @param email String for user email
     * @param state booleam for state information
     */
    public void setCurrentUserBean(String name, String email, boolean state) {
        this.currentUserBean.as().setAuth(state);
        this.currentUserBean.as().setName(name);
        this.currentUserBean.as().setEmail(email);

    }


}
