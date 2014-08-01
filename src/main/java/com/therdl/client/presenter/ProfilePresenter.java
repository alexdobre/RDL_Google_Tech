package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.ProfileView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * ProfilePresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the profile related data in and out of the client
 * for example the User's Avatar upload and subsequent image presentation
 *
 * @ ProfileView  profileView this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */
public class ProfilePresenter extends RdlAbstractPresenter<ProfileView> implements ProfileView.Presenter {

	public ProfilePresenter(ProfileView servicesView, AppController controller) {
		super(controller);
		this.view = servicesView;
		servicesView.setPresenter(this);
	}

	/**
	 * standard runtime method for MVP architecture
	 *
	 * @param container       the view container
	 * @param currentUserBean the user state bean, mainly used for authorisation
	 *                        and to update the menu
	 */
	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin(view.getAppMenu(),currentUserBean);
		container.clear();
		container.add(view.asWidget());
		view.getAppMenu().setAppMenu(currentUserBean);
	}


}
