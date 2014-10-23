package com.therdl.client.presenter;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.ServicesView;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * The services presenter
 */
public class ServicesPresenter extends RdlAbstractPresenter<ServicesView> implements ServicesView.Presenter, ServiceFilterRunt {

	private AutoBean<CurrentUserBean> currentUserBean;

	private WidgetHolder holder;

	public ServicesPresenter(ServicesView servicesView, AppController controller) {
		super(controller);
		this.currentUserBean = controller.getCurrentUserBean();
		this.view = servicesView;
		this.view.setPresenter(this);
		this.holder = WidgetHolder.getHolder();
	}

	@Override
	public void go(HasWidgets container, final AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		this.currentUserBean = currentUserBean;
		container.clear();
		if (view.getCurrentSearchOptionsBean() == null){
			view.setCurrentSearchOptionsBean(initSearchOptions());
		}
		if (view.getServicesFilter() == null) {
			//init the services filter
			view.setServicesFilter(holder.getServicesFilter(this));
		} else {
			//we just add ourselves as the runt
			holder.getServicesFilter(this);
		}
		if (view.getServicesFilter().getSearchOptions() == null) {
			//init the search options on the filter
			view.getServicesFilter().populate(view.getCurrentSearchOptionsBean());
		}

		container.add(view.asWidget());
		view.getAppMenu().setActive();
		searchSnips(view.getCurrentSearchOptionsBean());
	}

	@Override
	public void doPagination(boolean isNextPage, int pageIndex) {
		log.info("Search presenter - on pagination -BEGIN");
		int newPageIndex = isNextPage ? (pageIndex + 1) : (pageIndex - 1);
		view.getListWidget().setPageIndex(newPageIndex);
		view.getCurrentSearchOptionsBean().as().setPageIndex(newPageIndex);
		view.doFilterSearch();
	}

	/**
	 * Handles snips searching request | response
	 *
	 * @param searchOptionsBean : bean of the snip search options
	 */
	@Override
	public void searchSnips(final AutoBean<SnipBean> searchOptionsBean) {
		log.info("grabWelcomeSnip searchSnips: " + searchOptionsBean.as());

		grabSnipFunc.searchSnips(searchOptionsBean, new SnipListCallback() {

			public  void onBeanListReturned (ArrayList<AutoBean<SnipBean>> beanList){
				view.displaySnipList(beanList, searchOptionsBean.as().getPageIndex());
				PaginationHelper.showPaginationOnView(searchOptionsBean.as().getPageIndex(),
						beanList.size(), view.getListWidget());
			}

		});
	}

	private AutoBean<SnipBean> initSearchOptions() {
		AutoBean<SnipBean> searchOptions = beanery.snipBean();
		searchOptions.as().setPageIndex(0);
		searchOptions.as().setSortOrder(-1);
		searchOptions.as().setSortField("creationDate");
		searchOptions.as().setReturnSnipContent(false);
		searchOptions.as().setSnipType(SnipType.SERVICE.getSnipType());
		return searchOptions;
	}
}
