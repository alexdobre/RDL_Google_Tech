package com.therdl.client.presenter.func;

import com.google.gwt.http.client.RequestCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.callback.SnipCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.shared.beans.SnipBean;

/**
 * Functionality related to getting snips from the server
 */
public interface GrabSnipFunc {

	/**
	 * Grabs a snip from the server as per search options. If there are several snips returned it just grabs
	 * the first one.
	 *
	 * @param searchOptions the search options
	 * @param snipCallback  the callback to invoke once the snip has bean retrieved
	 */
	public void grabSnip(AutoBean<SnipBean> searchOptions, SnipCallback snipCallback);

	/**
	 * Updates a snip on the server
	 *
	 * @param updatedSnip    updated snip
	 * @param statusCallback
	 * @return null if update successful, the error code otherwise
	 */
	public void updateSnip(AutoBean<SnipBean> updatedSnip, StatusCallback statusCallback);

	/**
	 * Creates a new snip on the server
	 *
	 *
	 * @param createSnip     the snip to be created
	 * @param token
	 *@param statusCallback the callback to be invoked upon creation  @return
	 */
	public void createSnip(AutoBean<SnipBean> createSnip, String token, StatusCallback statusCallback);

	/**
	 * Executes a snip search on the DB
	 *
	 * @param searchOptionsBean the search options
	 * @param callback          the callback to invoke once the search is completed
	 */
	public void searchSnips(final AutoBean<SnipBean> searchOptionsBean, RequestCallback callback);
}
