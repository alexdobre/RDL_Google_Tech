package com.therdl.client.presenter.runt;

import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

/**
 * Logic behind the user's profile description
 */
public interface ProfileDescRunt {

	/**
	 * Populates the profile description for a user. If no description is found or if
	 * the description is empty then it uses a default description message.
	 * @param summernote
	 */
	public void grabProfileDesc (Summernote summernote);

	/**
	 * Updates the profile description for the given user
	 * @param summernote
	 */
	public void updateProfileDesc (Summernote summernote);
}
