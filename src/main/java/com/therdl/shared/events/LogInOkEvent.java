package com.therdl.shared.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Simple  log-in ok event no payload required
 * handlers use this event to update the status of the menu
 * for a log in successfully authorisation flow
 */
public class LogInOkEvent extends GwtEvent<LogInOkEventEventHandler> {


	private AutoBean<CurrentUserBean> currentUserBean;

	public LogInOkEvent(AutoBean<CurrentUserBean> currentUserBean) {
		this.currentUserBean = currentUserBean;
	}

	public AutoBean<CurrentUserBean> getCurrentUserBean() {
		return currentUserBean;
	}

	public static Type<LogInOkEventEventHandler> TYPE = new Type<LogInOkEventEventHandler>();

	@Override
	public Type<LogInOkEventEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogInOkEventEventHandler logInOkEventEventHandler) {
		logInOkEventEventHandler.onLogInOkEvent(this);
	}
}
