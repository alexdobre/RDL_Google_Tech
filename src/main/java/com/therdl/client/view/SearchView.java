package com.therdl.client.view;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.presenter.PaginationPresenter;
import com.therdl.client.view.widget.ListWidget;
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
 * @ void showSnipList(JsArray<JSOModel> snips)  display's the search result list as a JSON Array of
 * JSOModel objects, see com.therdl.shared.beans.JSOModel javadoc for this class
 * @ getInitialSnipList() display's the initial search result list
 */
public interface SearchView extends RdlView {

	public interface Presenter extends PaginationPresenter{
		void searchSnips(AutoBean<SnipBean> searchOptionsBean);

		AppController getController();
	}

	void setPresenter(Presenter presenter);

	Presenter getPresenter();

	AutoBean<CurrentUserBean> getCurrentUserBean();

	AutoBean<SnipBean> getCurrentSearchOptionsBean();

	void setCurrentSearchOptionsBean (AutoBean<SnipBean> snipBean);

	Widget asWidget();

	void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

	ListWidget getListWidget();

	void doFilterSearch();
}
