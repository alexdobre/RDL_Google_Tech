package com.therdl.client.presenter.runt;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.ValidatedView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Logic behind the user's profile description
 */
public interface ProfileDescRunt {

	/**
	 * Populates the profile description for a user. If no description is found or if
	 * the description is empty then it uses a default description message.
	 *
	 */
	public void grabProfileDesc(final ProfileView view, AutoBean<CurrentUserBean> currentUserBean);

	/**
	 * Updates the profile description for the given user and displays a message
	 *
	 * @param content       the content of the description
	 * @param username
	 * @param token
	 * @param validatedView the validated view to display the success or error message
	 */
	public void updateProfileDesc(String content, String username, String token, ValidatedView validatedView);
}
