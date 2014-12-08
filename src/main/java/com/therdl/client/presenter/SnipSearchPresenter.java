package com.therdl.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * SnipSearchPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 * @ SearchView  searchView this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ String currentSnipId  used to retrieve the users correct snip
 * @ AutoBean<SnipBean>  currentBean determines the authorisation state of the searchView when
 * presented
 * @ArrayList<JSOModel> jSonList, List of  JSOModel objects, see com.therdl.shared.beans.JSOModel javadoc for this class
 * @ void getInitialSnipList() gets the initila list of snips when view first comes into focus
 * @ searchSnips(final AutoBean<SnipBean> searchOptionsBean) performs a snip search
 */

public class SnipSearchPresenter extends RdlAbstractPresenter<SearchView>
		implements SearchView.Presenter, PaginationPresenter {

	public SnipSearchPresenter(SearchView searchView, AppController controller, String token) {
		super(controller);
		this.view = searchView;
		this.view.setPresenter(this);
		this.view.setCurrentSearchOptionsBean(RDLUtils.parseSearchToken(beanery, token, null));
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		Log.info("SnipSearchPresenter go adding view");
		checkLogin(null);
		container.clear();
		view.getAppMenu().setActive();
		searchSnips(view.getCurrentSearchOptionsBean());
		container.add(view.asWidget());
	}

	/**
	 * Handles snips searching request | response
	 *
	 * @param searchOptionsBean : bean of the snip search options
	 */
	@Override
	public void searchSnips(final AutoBean<SnipBean> searchOptionsBean) {
		Log.info("grabWelcomeSnip searchSnips: " + searchOptionsBean.as());

		grabSnipFunc.searchSnips(searchOptionsBean, new SnipListCallback() {

			public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
				view.displaySnipList(beanList, searchOptionsBean.as().getPageIndex());
				PaginationHelper.showPaginationOnView(searchOptionsBean.as().getPageIndex(),
						beanList.size(), view.getListWidget());
			}

		});
	}

	@Override
	public void doPagination(boolean isNextPage, int pageIndex) {
		Log.info("Search presenter - on pagination -BEGIN");
		int newPageIndex = isNextPage ? (pageIndex + 1) : (pageIndex - 1);
		view.getListWidget().setPageIndex(newPageIndex);
		view.getCurrentSearchOptionsBean().as().setPageIndex(newPageIndex);
		view.doFilterSearch();
	}
}
