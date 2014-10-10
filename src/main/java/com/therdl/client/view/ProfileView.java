package com.therdl.client.view;

import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * see com.therdl.client.view.impl.ProfileViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * this method sets the options in the header/nav bar
 * @ setAvatarWhenViewIsNotNull( ) sets the avatar image when the profile view comes into focus
 */
public interface ProfileView extends RdlView, ValidatedView {


	public interface Presenter {
		/**
		 * Validates and saves a new password
		 * @param oldPass the old password to be checked
		 * @param newPass the new password
		 * @param newPassConfirm must match the new password
		 * @return null if save OK, error message otherwise
		 */
		public String changePassword(AutoBean<CurrentUserBean> currentUserBean,
		                             String oldPass, String newPass, String newPassConfirm);

		public void updateProfile(String content);
	}

	void setPresenter(Presenter presenter);

	public void populateView(AutoBean<CurrentUserBean> currentUserBean);

	public void setFormSuccessMsg (String msg);

	public void setChangePassErrorMsg (String msg);

	public void populateProfileDescription (String content);
}
