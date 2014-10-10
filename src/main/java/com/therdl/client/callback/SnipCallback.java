package com.therdl.client.callback;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Invoked when a single snipBean is returned from the DB
 */
public interface SnipCallback {

	/**
	 * When a snip bean has successfully returned from the db
	 * @param snipBean
	 */
	public void onSnipBeanReturned(AutoBean<SnipBean> snipBean);
}
