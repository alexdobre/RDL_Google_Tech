package com.therdl.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.presenter.*;

import com.therdl.client.view.RegisterView;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.impl.RegisterViewImpl;
import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.client.view.impl.WelcomeViewImpl;
import com.therdl.shared.Messages;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogOutEvent;
import com.therdl.shared.events.LogOutEventEventHandler;

import java.util.logging.Logger;

public class AppController implements Presenter, ValueChangeHandler<String>{

    private static Logger log = Logger.getLogger("");

	private HasWidgets container;

    private Beanery beanery = GWT.create(Beanery.class);

	/**
	 * Current authentication rules are anyone can view bit only registered user can edit
	 */
	private WelcomeView welcomeView;
    private SnipEditView  snipEditView;
    private SnipSearchView  snipSearchView;
    private AutoBean<AuthUserBean> authnBean = beanery.authBean();
    private RegisterView registerView;
    /**
     * seperate authorisation from user state with 2 user beans, currrent user and authorised user
     * for example auth user will have initial password for sign up
     * current user as a persistant client side bean will not contain password info and any time
     */
    private AutoBean<AuthUserBean> authBean = beanery.authBean();
    // temp solution until IE 7, 8, 9  drop off the radar
    private  AutoBean<CurrentUserBean> currentUserBean = beanery.currentUserBean();


	
	public AppController() {

		bind();
	}
	  
	/**
	 * Binds the event handler instances to their specific events
	 */
	private void bind() {

		//register yourself as history value change handler
		History.addValueChangeHandler(this);

        log.info("AppController bind() addValueChangeHandler");

        GuiEventBus.EVENT_BUS.addHandler(LogOutEvent.TYPE, new LogOutEventEventHandler()  {
            @Override
            public void onLogOutEvent(LogOutEvent onLogOutEvent) {
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

                if (welcomeView == null) {
                    welcomeView = new WelcomeViewImpl( currentUserBean);
                }
                // in async call back this is not app controller
                final WelcomePresenter welcomePresenter =  new WelcomePresenter(welcomeView, this);

                log.info("AppController onValueChange Tokens.WELCOME) past line 109 ");

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {

                        welcomePresenter.go(container);
                        // use app menu

                        // check user status if auth no login
                        if(currentUserBean.as().isAuth()) {
                            // use app menu
                            welcomeView.setloginresult(currentUserBean.as().getName(), currentUserBean.as().getEmail(), true);
                        }
                    }
                });

            }


            //***************************************SNIPS****************************
            else if (token.equals(RDLConstants.Tokens.SNIPS)) {
                if (snipSearchView == null) {
                     snipSearchView = new SnipSearchViewImpl(currentUserBean);
                }

                final SnipSearchPresenter snipSearchPresenter =  new SnipSearchPresenter(snipSearchView, this);

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                        log.info("AppController GWT.runAsync onFailure "+RDLConstants.Tokens.SNIPS);
                    }

                    public void onSuccess() {

                            snipSearchPresenter.go(container);

                    }
                });

            }// end else

            //***************************************SNIP_EDIT****************************
            // only authorised users
            else if (token.equals(RDLConstants.Tokens.SNIP_EDIT)) {
                // in async call back this is not app controller
                if (snipEditView == null) {
                    snipEditView = new SnipEditViewImpl(currentUserBean);
                }
                final SnipEditPresenter snipEditPresenter =  new SnipEditPresenter(snipEditView, this);


                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        if(currentUserBean.as().isAuth())  {
                            snipEditPresenter.go(container);
                        } else {
                            History.newItem(RDLConstants.Tokens.WELCOME);
                        }
                    }
                });
            }

            //*************************************** LOG_OUT ****************************



            else if (token.equals(RDLConstants.Tokens.LOG_OUT)) {

                final WelcomePresenter welcomePresenter =  new WelcomePresenter(welcomeView, this);


                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        currentUserBean.as().setAuth(false);
                        if (welcomeView == null) {
                            welcomeView = new WelcomeViewImpl( currentUserBean);

                        }
                        currentUserBean.as().setAuth(false);
                        welcomePresenter.go(container);
                        welcomeView.logout();
                    }
                });
            }



            //*************************************** LOG_OUT ****************************


            else if (token.equals(RDLConstants.Tokens.SIGN_UP)) {

                if (registerView == null) {
                    registerView = new RegisterViewImpl();
                    currentUserBean.as().setAuth(false);
                }
                // in async call back this is not app controller
                final RegisterPresenter registerPresenter =  new RegisterPresenter(registerView, this);
                log.info("AppController Tokens.SIGN_UP ");
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        registerPresenter.go(container);

                    }
                });
            }




        } // end  if token != null



    }// end method


    public AutoBean<CurrentUserBean> getCurrentUserBean() {
        return currentUserBean;
    }

    public void setCurrentUserBean(String name, String email, boolean state) {
        this.currentUserBean.as().setAuth(state);
        this.currentUserBean.as().setName(name);
        this.currentUserBean.as().setEmail(email);
    }



}
