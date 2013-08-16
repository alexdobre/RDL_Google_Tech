package com.therdl.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

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

import java.util.logging.Logger;

public class AppController implements Presenter, ValueChangeHandler<String>{

    private static Logger log = Logger.getLogger("");
	
	private final EventBus eventBus;
	private final Messages messages;

	private HasWidgets container;

	/**
	 * All the application views go here and are kept to be reused once created
	 */
	private WelcomeView welcomeView;
    private SnipEditView  snipEditView;
    private SnipSearchView  snipSearchView;

	
	public AppController(EventBus eventBus) {
		this.eventBus = eventBus;
		messages = (Messages) GWT.create(Messages.class);
		bind();
	}
	  
	/**
	 * Binds the event handler instances to their specific events
	 */
	private void bind() {
		//register yourself as history value change handler
		History.addValueChangeHandler(this);

        log.info("AppController bind() addValueChangeHandler(");

	}
	
	@Override
	public void go(final HasWidgets container) {
		this.container = container;
		
		//when first accessed this app goes to the welcome page
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
							welcomeView = new WelcomeViewImpl(eventBus,messages);
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
                        snipSearchView = new SnipSearchViewImpl(eventBus);
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
                        new SnipEditPresenter( eventBus, snipEditView).go(container);
                    }
                });
            }


            //*************************************** END_SNIPS ****************************


            //*************************************** SIGN_IN ****************************





        } // end  if token != null



    }// end method

	public EventBus getEventBus() {
		return eventBus;
	}

}
