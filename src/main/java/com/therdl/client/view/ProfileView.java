package com.therdl.client.view;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
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
public interface ProfileView extends RdlView {


	public interface Presenter {

	}

	void setPresenter(Presenter presenter);

	public void populateView(AutoBean<CurrentUserBean> currentUserBean);
}
