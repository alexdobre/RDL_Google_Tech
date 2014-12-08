package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipCallback;
import com.therdl.client.view.PublicProfileView;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * For the users public profile description
 */
public class PublicProfilePresenter extends RdlAbstractPresenter<PublicProfileView> implements PublicProfileView.Presenter {

	private AutoBean<CurrentUserBean> currentUserBean;
	private String username;

	public PublicProfilePresenter(PublicProfileView profileView, AppController controller, String username) {
		super(controller);
		this.view = profileView;
		this.username = username;
		profileView.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin(null);
		this.currentUserBean = currentUserBean;
		container.clear();

		AutoBean<SnipBean> searchOptionsBean = buildUserProfileSearchOptions(username);

		grabSnipFunc.grabSnip(searchOptionsBean, new SnipCallback() {
			@Override
			public void onSnipBeanReturned(AutoBean<SnipBean> snipBean) {
				if (snipBean == null) {
					view.populateProfileDescription(RDL.getI18n().notCompletedProfileDesc());
				} else if (!snipBean.as().getAuthorSupporter()) {
					view.populateProfileDescription(RDL.getI18n().notSupporterProfileDesc());
				} else {
					view.populateProfileDescription(snipBean.as().getContent());
				}
			}
		});

		container.add(view.asWidget());
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

}
