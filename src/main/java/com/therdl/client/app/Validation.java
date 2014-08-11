package com.therdl.client.app;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Static class for validating objects
 *
 * @ resetCurrentUserBeanFields  (AutoBean<CurrentUserBean> currentUserBean)
 * cleans the User state information, useful after a log out event for example
 */
public class Validation {

	/**
	 * @param currentUserBean AutoBean see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
	 * @return
	 */
	static AutoBean<CurrentUserBean> resetCurrentUserBeanFields(AutoBean<CurrentUserBean> currentUserBean) {
		currentUserBean.as().setAuth(false);
		currentUserBean.as().setName("");
		currentUserBean.as().setEmail("");
		currentUserBean.as().setRegistered(false);
		return currentUserBean;
	}


}
