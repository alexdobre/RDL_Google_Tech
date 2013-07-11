package com.therdl.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import com.therdl.client.presenter.Presenter;

import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.impl.WelcomeViewImpl;
import com.therdl.client.presenter.WelcomePresenter;
import com.therdl.shared.Messages;
import com.therdl.shared.RDLConstants;

public class AppController implements Presenter, ValueChangeHandler<String>{
	
	private final EventBus eventBus;
	private final Messages messages;

	private HasWidgets container;

	/**
	 * All the application views go here and are kept to be reused once created
	 */
	private WelcomeView welcomeView;

	
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

		// eventBus.addHandler(AddContactEvent.TYPE,
		// new AddContactEventHandler() {
		// public void onAddContact(AddContactEvent event) {
		// doAddNewContact();
		// }
		// });
		//
		// eventBus.addHandler(EditContactEvent.TYPE,
		// new EditContactEventHandler() {
		// public void onEditContact(EditContactEvent event) {
		// doEditContact(event.getId());
		// }
		// });
		//
		// eventBus.addHandler(EditContactCancelledEvent.TYPE,
		// new EditContactCancelledEventHandler() {
		// public void onEditContactCancelled(EditContactCancelledEvent event) {
		// doEditContactCancelled();
		// }
		// });
		//
		// eventBus.addHandler(ContactUpdatedEvent.TYPE,
		// new ContactUpdatedEventHandler() {
		// public void onContactUpdated(ContactUpdatedEvent event) {
		// doContactUpdated();
		// }
		// });
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
	//***************************************SNIPS****************************			
			}
	}
    }

	public EventBus getEventBus() {
		return eventBus;
	}

}
