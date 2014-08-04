package com.therdl.client.view.impl;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.widget.AppMenu;

/**
 * Encapsulates functionality for a view which contains the app menu
 */
public abstract class AppMenuView extends Composite {

	@UiField
	SimplePanel appMenuPanel;
	AppMenu appMenu;

	public AppMenuView(AppMenu appMenu) {
		this.appMenu = appMenu;
	}

	@Override
	public Widget asWidget(){
		this.appMenuPanel.clear();
		this.appMenuPanel.add(appMenu);
		return super.asWidget();
	}

	public AppMenu getAppMenu() {
		return this.appMenu;
	}

	/**
	 * Sets the upper header Menu to the correct state for supplied credentials
	 * post sign up called from presenter
	 *
	 * @param name  supplied credential
	 * @param email supplied credential
	 * @param auth  boolean auth state from server via presenter
	 */
	public void setLoginResult(String name, String email, boolean auth) {
		if (auth) {
			this.appMenu.setLogOutVisible(true);
			this.appMenu.setSignUpVisible(false);
			this.appMenu.setUserInfoVisible(true);
			this.appMenu.setUser(name);
			this.appMenu.setEmail(email);
			this.appMenu.setLogInVisible(false);
		} else {
			this.appMenu.setLogOutVisible(false);
			this.appMenu.setSignUpVisible(true);
			this.appMenu.setUserInfoVisible(false);
			this.appMenu.setLogInVisible(true);
		}
	}

}
