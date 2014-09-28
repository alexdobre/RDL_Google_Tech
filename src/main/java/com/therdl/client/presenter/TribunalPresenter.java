package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.view.TribunalView;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * Logic behind the tribunal view
 */
public class TribunalPresenter extends RdlAbstractPresenter<TribunalView> implements TribunalView.Presenter, PaginationPresenter {

	public TribunalPresenter(TribunalView tribunalView, AppController controller) {
		super(controller);
		this.view = tribunalView;
		this.view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		log.info("SnipSearchPresenter go adding view");
		checkLogin();
		container.clear();
		view.getAppMenu().setActive();
		if (view.getCurrentSearchOptionsBean() == null) {
			view.setCurrentSearchOptionsBean(initSearchOptions());
		}
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
		log.info("grabWelcomeSnip searchSnips: " + searchOptionsBean.as());

		super.searchAbuse(searchOptionsBean, new SnipListCallback() {

			public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
				view.displaySnipList(beanList, searchOptionsBean.as().getPageIndex());
				PaginationHelper.showPaginationOnView(searchOptionsBean.as().getPageIndex(),
						beanList.size(), view.getListWidget());
			}

		});
	}

	@Override
	public void doPagination(boolean isNextPage, int pageIndex) {
		log.info("Search presenter - on pagination -BEGIN");
		int newPageIndex = isNextPage ? (pageIndex + 1) : (pageIndex - 1);
		view.getListWidget().setPageIndex(newPageIndex);
		view.getCurrentSearchOptionsBean().as().setPageIndex(newPageIndex);
		view.doFilterSearch();
	}

	private AutoBean<SnipBean> initSearchOptions() {
		log.info("Init search options bean");
		AutoBean<SnipBean> searchOptions = beanery.snipBean();
		searchOptions.as().setPageIndex(0);
		searchOptions.as().setReturnSnipContent(false);
		searchOptions.as().setSnipType(SnipType.TRIBUNAL.getSnipType());
		searchOptions.as().setSortOrder(-1);
		searchOptions.as().setSortField("votingExpiresDate");
		searchOptions.as().setVotingExpired(false);
		return searchOptions;
	}
}
