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
	private final Messages messages;

	private HasWidgets container;

    private Beanery beanery = GWT.create(Beanery.class);

	/**
	 * All the application views go here and are kept to be reused once created
	 */
	private WelcomeView welcomeView;
    private SnipEditView  snipEditView;
    private SnipSearchView  snipSearchView;
    private AutoBean<AuthUserBean> authnBean = beanery.authBean();
    private RegisterView registerView;
    private AutoBean<AuthUserBean> authBean = beanery.authBean();
    // temp solution until IE 7, 8, 9  drop off the radar
    private  AutoBean<CurrentUserBean> currentUserBean = beanery.currentUserBean();

	
	public AppController() {

		messages = (Messages) GWT.create(Messages.class);
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
        // for now force the login screen
        authnBean.as().setAuth(false);

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
                    welcomeView = new WelcomeViewImpl(messages, authBean);
                }
                // in async call back this is not app controller
                final WelcomePresenter welcomePresenter =  new WelcomePresenter(welcomeView, this);
                log.info("AppController onValueChange Tokens.WELCOME) past line 109 ");

                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {

                        welcomePresenter.go(container);
                        welcomeView.getSignInView().setSignIsVisible(true);
                        // check user status if auth no login
                        if(currentUserBean.as().isAuth()) {
                            welcomeView.getSignInView().setSignIsVisible(false);
                            welcomeView.setloginresult(currentUserBean.as().getName(),currentUserBean.as().getEmail(), true);
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
                        if(currentUserBean.as().isAuth())  {
                            snipSearchPresenter.go(container);
                        } else {
                            History.newItem(RDLConstants.Tokens.WELCOME);
                        }
                    }
                });
            }// end el

            //***************************************SNIP_EDIT****************************

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
                    }
                        else {

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
                        authBean.as().setAuth(false);
                        if (welcomeView == null) {
                            welcomeView = new WelcomeViewImpl(messages, authBean);

                        }
                        currentUserBean.as().setAuth(false);
                        welcomePresenter.go(container);
                        welcomeView.logout();
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
                        authBean.as().setAuth(false);
                        currentUserBean.as().setAuth(false);
                        if (welcomeView == null) {
                            welcomeView = new WelcomeViewImpl(messages, authBean);

                        }

                        welcomePresenter.go(container);
                        welcomeView.logout();
                    }
                });
            }


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



    //*************************************** Check Sign in  ****************************



    public void checkUserAuth(String id) {

        log.info("AppController checkUserAuthsnip id "+id);
        String authUrl = GWT.getModuleBaseURL() + "getSession";
        authUrl = authUrl.replaceAll("/therdl", "");

        log.info("SnipEditorWorkflow submit updateUrl: " + authUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(authUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        try {
            AutoBean<SnipBean> actionBean = beanery.snipBean();
//            actionBean.as().setAction("getUserAuth");
            actionBean.as().setId(id);
            String json = AutoBeanCodex.encode(actionBean).getPayload();

            log.info("SnipEditorWorkflow submit json: " + json);
            requestBuilder.sendRequest(json , new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    if (response.getStatusCode() == 200) {
                        // ok move forward
                        log.info("SnipEditorWorkflow submit post ok now validating");


                    } else {
                        log.info("SnipEditorWorkflow submit post fail");

                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("SnipEditorWorkflow submit onError)" + exception.getLocalizedMessage());

                }

            });
        } catch (RequestException e) {
            log.info(e.getLocalizedMessage());
        }


    }



    public AutoBean<CurrentUserBean> getCurrentUserBean() {
        return currentUserBean;
    }

    public void setCurrentUserBean(String name, String email, boolean state) {
        this.currentUserBean.as().setAuth(state);
        this.currentUserBean.as().setName(name);
        this.currentUserBean.as().setEmail(email);
    }



}
