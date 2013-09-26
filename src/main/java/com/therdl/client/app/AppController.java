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
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogOutEvent;
import com.therdl.shared.events.LogOutEventEventHandler;

import java.util.logging.Logger;

public class AppController implements Presenter, ValueChangeHandler<String>{

    private static Logger log = Logger.getLogger("");

	private HasWidgets container;

    private Beanery beanery = GWT.create(Beanery.class);

	/**
	 * Current authentication rules are anyone can view but only registered user can edit
	 */
    private AutoBean<AuthUserBean> authnBean = beanery.authBean();

    /**
     * seperate authorisation from user state with 2 user beans, current user and authorised user
     * for example auth user will have initial password for sign up
     * current user as a persistant client side bean will not contain password info and any time
     */
    private  AutoBean<CurrentUserBean> currentUserBean = beanery.currentUserBean();

	private WelcomeView welcomeView;
    private SnipEditView  snipEditView;
    private SnipSearchView  snipSearchView;
    private RegisterView registerView;
    private ProfileView profileView;
	
	public AppController() {

		bind();
	}
	  
	/**
	 * Binds the event handler instances to their specific events
	 */
	private void bind() {

		History.addValueChangeHandler(this);

        log.info("AppController bind() addValueChangeHandler");

        // logout event handler
        GuiEventBus.EVENT_BUS.addHandler(LogOutEvent.TYPE, new LogOutEventEventHandler()  {
            @Override
            public void onLogOutEvent(LogOutEvent onLogOutEvent) {
                currentUserBean.as().setAuth(false);
                History.newItem(RDLConstants.Tokens.LOG_OUT);
                History.fireCurrentHistoryState();
            }
        });

	}
	
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
	 */
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

        log.info("AppController onValueChange token is  "+token);

		if (token != null) {

            //***************************************WELCOME****************************
            if (token.equals(RDLConstants.Tokens.WELCOME)) {
                log.info("AppController Tokens.WELCOME");
                if (welcomeView == null) {
                    welcomeView = new WelcomeViewImpl( currentUserBean);
                }
                final WelcomePresenter welcomePresenter =  new WelcomePresenter(welcomeView, this);
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {

                        welcomePresenter.go(container, currentUserBean);

                        if(currentUserBean.as().isAuth()) {
                            welcomeView.setloginresult(currentUserBean.as().getName(), currentUserBean.as().getEmail(), true);
                        }
                    }
                });
            }
            //***************************************SNIPS****************************
            else if (token.equals(RDLConstants.Tokens.SNIPS)) {
                log.info("AppController Tokens.SNIPS");
                if (snipSearchView == null) {
                     snipSearchView = new SnipSearchViewImpl(currentUserBean);
                }

                final SnipSearchPresenter snipSearchPresenter =  new SnipSearchPresenter(snipSearchView, this);

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                        log.info("AppController GWT.runAsync onFailure "+RDLConstants.Tokens.SNIPS);
                    }

                    public void onSuccess() {

                            snipSearchPresenter.go(container, currentUserBean);

                    }
                });

            }// end else

            //***************************************SNIP_EDIT****************************
            // only authorised users
            else if (token.equals(RDLConstants.Tokens.SNIP_EDIT)) {
                log.info("AppController Tokens.SNIP_EDIT");
                if (snipEditView == null) {
                    snipEditView = new SnipEditViewImpl(currentUserBean);
                }
                final SnipEditPresenter snipEditPresenter =  new SnipEditPresenter(snipEditView, this);


                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        if(currentUserBean.as().isAuth())  {
                            snipEditPresenter.go(container, currentUserBean);
                        } else {
                            History.newItem(RDLConstants.Tokens.WELCOME);
                        }
                    }
                });
            }

            //*************************************** SIGN_UP ****************************
            else if (token.equals(RDLConstants.Tokens.SIGN_UP)) {

                if (registerView == null) {
                    registerView = new RegisterViewImpl();
                    currentUserBean.as().setAuth(false);
                }

                final RegisterPresenter registerPresenter =  new RegisterPresenter(registerView, this);
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
                profileView = new ProfileViewImpl(currentUserBean);

            }

            final ProfilePresenter profilePresenter =  new ProfilePresenter(profileView);
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

                final WelcomePresenter welcomePresenter =  new WelcomePresenter(welcomeView, this);
                log.info("AppController Tokens.LOG_OUT ");
                if (welcomeView != null ) {

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
                            welcomeView = new WelcomeViewImpl( currentUserBean);

                        }
                        currentUserBean.as().setAuth(false);
                        welcomePresenter.go(container, currentUserBean);
                        welcomeView.logout();
                    }
                });
            }





        } // end  if token != null

    }// end onValueChanged

    public AutoBean<CurrentUserBean> getCurrentUserBean() {
        return currentUserBean;
    }

    public void setCurrentUserBean(String name, String email, boolean state) {
        this.currentUserBean.as().setAuth(state);
        this.currentUserBean.as().setName(name);
        this.currentUserBean.as().setEmail(email);
    }

}
