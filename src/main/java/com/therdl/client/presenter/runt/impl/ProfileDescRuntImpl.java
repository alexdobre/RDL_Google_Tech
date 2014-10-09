package com.therdl.client.presenter.runt.impl;

import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.presenter.func.FuncFactory;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.presenter.runt.ProfileDescRunt;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Logic behind the user's profile description
 */
public class ProfileDescRuntImpl implements ProfileDescRunt {

	private AutoBean<CurrentUserBean> currentUserBean;

	public void grabProfileDesc (Summernote summernote) {
		summernote.setPlaceholder(RDL.getI18n().defaultProfileDescription());

		AutoBean<SnipBean> userProfile = getUserProfile(currentUserBean.as().getName(), currentUserBean.as().getEmail());
		if (userProfile != null && userProfile.as().getContent() != null) {
			summernote.setCode(userProfile.as().getContent());
		}
	}

	public void updateProfileDesc (Summernote summernote) {
		GrabSnipFunc grabSnipFunc = FuncFactory.createGrabSnipFunc();
		//TODO implement ProfileDescRuntImpl.updateProfileDesc
	}

	private AutoBean<SnipBean> getUserProfile(String username, String email) {
		//TODO implement ProfileDescRuntImpl.getUserProfile
		return null;
	}
}
