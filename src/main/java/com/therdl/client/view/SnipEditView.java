package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * see com.therdl.client.view.impl.ProfileViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ AppMenu getAppMenu() returns the Nav-bar header using the user authorisation status
 * this method sets the options in the header/nav bar AppMenu widget
 */
public interface SnipEditView extends RdlView, ValidatedView, EmotionView {

	public interface Presenter {
		void onDeleteSnip(AutoBean<SnipBean> bean, AutoBean<CurrentUserBean> currentUserBean);

		void submitBean(AutoBean<SnipBean> bean, AutoBean<CurrentUserBean> currentUserBean);

		void submitEditedBean(AutoBean<SnipBean> bean, AutoBean<CurrentUserBean> currentUserBean);
	}

	public void setPresenter(Presenter presenter);

	public void populate(AutoBean<SnipBean> snipBean);

	public void showHideCategories(Boolean show);

	public void showHideIdeaTypes(Boolean show);

	public void showHideImprovementPanels(Boolean show);

	public Widget asWidget();
}
