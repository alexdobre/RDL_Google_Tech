package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.ExploreView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The logic behind the explore view
 *
 * @author alex
 */
public class ExplorePresenter extends RdlAbstractPresenter<ExploreView> implements ExploreView.Presenter {

	public ExplorePresenter(ExploreView exploreView, AppController controller, String[] tokenSplit) {
		super(controller);
		this.view = exploreView;
		this.view.setPresenter(this);
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
		checkLogin(null);
		container.clear();
		view.getAppMenu().allInactive();

		container.add(view.asWidget());
	}
}
