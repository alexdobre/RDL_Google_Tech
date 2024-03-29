package com.therdl.client.view;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.beans.SnipBean;

/**
 * see com.therdl.client.view.impl.WelcomeViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ showLoginFail() displays the validation method when the login fails eg password incorrect
 * * @ showForgotPasswordPopUp() displays the popup form in forgetting password.
 * @ AppMenu getAppMenu() returns the Nav-bar header using the user authorisation status
 * @ setloginresult(String name, String email, boolean auth) sets the options in the header/nav bar
 * using the user sign up result status
 */
public interface WelcomeView extends RdlView{

	public interface Presenter {
		/**
		 * Grabs the welcome snip based on the core category passed in
		 * @param coreCat the core category for which to grab
		 * @param title the title of the snip - may be NULL
		 */
		public void grabWelcomeSnip(final CoreCategory coreCat, String title);
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

	void setDelayedDisplay(CoreCategory cat);

	/**
	 * Shows the user the welcome snip
	 * @param coreCat
	 */
	public void showWelcomeSnip (AutoBean<SnipBean> welcomeSnip ,CoreCategory coreCat);

	public Map<CoreCategory,AutoBean<SnipBean>> getSnipMap();
}
