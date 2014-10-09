package com.therdl.client.presenter.func;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Functionality related to getting snips from the server
 */
public interface GrabSnipFunc {

	/**
	 * Grabs a snip from the server as per search options. If there are several snips returned it just grabs
	 * the first one.
	 * @param searchOptions the search options
	 * @return the grabed snip or null if snip not found
	 */
	public AutoBean<SnipBean> grabSnip(AutoBean<SnipBean> searchOptions);
}
