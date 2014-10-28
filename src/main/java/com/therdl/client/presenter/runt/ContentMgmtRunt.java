package com.therdl.client.presenter.runt;

import com.google.gwt.user.client.ui.Panel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Logic behind content shown on the site
 */
public interface ContentMgmtRunt {

	/**
	 * Grab a piece of content from the DB and display it at location
	 *
	 * @param searchOptions the options to search by
	 * @param location      where to display
	 */
	public void grabAndDisplay(AutoBean<SnipBean> searchOptions, Panel location);

	/**
	 * Grab and display the sign up message description
	 * @param location where to display
	 */
	public void displaySignInMsg(Panel location);
}
