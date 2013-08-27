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
import com.therdl.client.presenter.Presenter;

import com.therdl.client.presenter.SnipEditPresenter;
import com.therdl.client.presenter.SnipSearchPresenter;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.client.view.impl.WelcomeViewImpl;
import com.therdl.client.presenter.WelcomePresenter;
import com.therdl.shared.Messages;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
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
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess() {
						if (welcomeView == null) {
							welcomeView = new WelcomeViewImpl(messages, authnBean);
						}
						new WelcomePresenter(welcomeView).go(container);						
					}
				});

			}


    //***************************************SNIPS****************************

        else if (token.equals(RDLConstants.Tokens.SNIPS)) {
            GWT.runAsync(new RunAsyncCallback() {
                public void onFailure(Throwable caught) {
                    log.info("AppController GWT.runAsync onFailure "+RDLConstants.Tokens.SNIPS);
                }

                public void onSuccess() {
                    log.info("AppController GWT.runAsync onSuccess "+RDLConstants.Tokens.SNIPS);
                    if (snipSearchView == null) {
                        snipSearchView = new SnipSearchViewImpl();
                    }
                    new SnipSearchPresenter(snipSearchView).go(container);

                }
            });
        }// end else

            else if (token.equals(RDLConstants.Tokens.SNIP_EDIT)) {
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        if (snipEditView == null) {
                            snipEditView = new SnipEditViewImpl();
                        }
                        new SnipEditPresenter( snipEditView).go(container);
                    }
                });
            }


            //*************************************** END_SNIPS ****************************


            //*************************************** SIGN_IN ****************************



            else if (token.equals(RDLConstants.Tokens.LOG_OUT)) {
                GWT.runAsync(new RunAsyncCallback() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess() {
                        authnBean.as().setAuth(false);
                        if (welcomeView == null) {
                            welcomeView = new WelcomeViewImpl(messages, authnBean);
                        }

                        new WelcomePresenter(welcomeView).go(container);
                        welcomeView.logout();
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

}
