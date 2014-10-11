package com.therdl.client.presenter.runt.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.callback.SnipCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.presenter.func.FuncFactory;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.presenter.runt.ProfileDescRunt;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.ValidatedView;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;

/**
 * Logic behind the user's profile description
 */
public class ProfileDescRuntImpl implements ProfileDescRunt {
	protected static Logger log = Logger.getLogger(ProfileDescRuntImpl.class.getName());

	private Beanery beanery = GWT.create(Beanery.class);
	private AutoBean<SnipBean> profileDesc;

	@Override
	public void grabProfileDesc(final ProfileView view, AutoBean<CurrentUserBean> currentUserBean) {
		GrabSnipFunc grabSnipFunc = FuncFactory.createGrabSnipFunc();
		AutoBean<SnipBean> searchOptionsBean = buildUserProfileSearchOptions(currentUserBean.as().getName());

		grabSnipFunc.grabSnip(searchOptionsBean, new SnipCallback() {
			@Override
			public void onSnipBeanReturned(AutoBean<SnipBean> snipBean) {
				if (snipBean == null) {
					log.info("user profile null - setting default");
					view.populateProfileDescription(RDL.getI18n().defaultProfileDescription());
				} else {
					view.populateProfileDescription(snipBean.as().getContent());
					profileDesc = snipBean;
				}
			}
		});
	}

	@Override
	public void updateProfileDesc(String content, String username, String token, final ValidatedView validatedView) {
		GrabSnipFunc grabSnipFunc = FuncFactory.createGrabSnipFunc();
		if (profileDesc == null || profileDesc.as().getId() == null) {
			//this must be the first time the user saves the profile desc so we build it for him
			grabSnipFunc.createSnip(buildProfileBean(content, username), token, new StatusCallback(validatedView) {
				@Override
				public void onSuccess(Request request, Response response) {
					validatedView.setSuccessMessage(RDL.getI18n().profileDescriptionUpdateSuccess());
				}
			});
		} else {
			profileDesc.as().setContent(content);
			grabSnipFunc.updateSnip(profileDesc, new StatusCallback(validatedView) {
				@Override
				public void onSuccess(Request request, Response response) {
					validatedView.setSuccessMessage(RDL.getI18n().profileDescriptionUpdateSuccess());
				}
			});
		}
	}

	/**
	 * Builds the search options bean to retrieve a user's profile
	 *
	 * @param username the user's name
	 */
	private AutoBean<SnipBean> buildUserProfileSearchOptions(String username) {
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		searchOptionsBean.as().setAuthor(username);
		searchOptionsBean.as().setSnipType(SnipType.PROFILE.getSnipType());
		return searchOptionsBean;
	}

	private AutoBean<SnipBean> buildProfileBean(String content, String username) {
		AutoBean<SnipBean> result = beanery.snipBean();
		result.as().setContent(content);
		result.as().setSnipType(SnipType.PROFILE.getSnipType());
		result.as().setCoreCat(RDLConstants.INTERNAL);
		result.as().setAuthor(username);
		result.as().setTitle(RDL.getI18n().yourProfile() + " " + username);
		return result;
	}
}
