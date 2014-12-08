package com.therdl.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.app.LogicHolder;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.logic.LinkedSortBit;
import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.ServicesView;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.runtized.ServicesFilter;
import com.therdl.client.view.widget.runtized.SortBit;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * The services presenter
 */
public class ServicesPresenter extends RdlAbstractPresenter<ServicesView>
		implements ServicesView.Presenter, ServiceFilterRunt {

	private AutoBean<CurrentUserBean> currentUserBean;

	private WidgetHolder widgetHolder;
	private LogicHolder logicHolder;

	public ServicesPresenter(ServicesView servicesView, AppController controller) {
		super(controller);
		this.currentUserBean = controller.getCurrentUserBean();
		this.view = servicesView;
		this.view.setPresenter(this);
		this.widgetHolder = WidgetHolder.getHolder();
		this.logicHolder = LogicHolder.getHolder();
	}

	@Override
	public void go(HasWidgets container, final AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin(null);
		this.currentUserBean = currentUserBean;
		container.clear();
		if (view.getCurrentSearchOptionsBean() == null) {
			view.setCurrentSearchOptionsBean(initSearchOptions());
		}
		if (view.getServicesFilter() == null) {
			//init the services filter
			view.setServicesFilter(widgetHolder.getServicesFilter(this));
		} else {
			//we just add ourselves as the runt
			widgetHolder.getServicesFilter(this);
		}
		if (view.getServicesFilter().getSearchOptions() == null) {
			//init the search options on the filter
			view.getServicesFilter().populate(view.getCurrentSearchOptionsBean());
		}
		if (logicHolder.getServiceFilterLinkedSortBit() == null) {
			initLinkedSortBit();
		}
		logicHolder.getServiceFilterLinkedSortBit().passRunt(this);
		container.add(view.asWidget());
		view.getAppMenu().setActive();
		searchSnips(view.getCurrentSearchOptionsBean());
	}

	@Override
	public void doPagination(boolean isNextPage, int pageIndex) {
		Log.info("Search presenter - on pagination -BEGIN");
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
		Log.info("grabWelcomeSnip searchSnips: " + searchOptionsBean.as());

		grabSnipFunc.searchSnips(searchOptionsBean, new SnipListCallback() {

			public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
				view.displaySnipList(beanList, searchOptionsBean.as().getPageIndex());
				PaginationHelper.showPaginationOnView(searchOptionsBean.as().getPageIndex(),
						beanList.size(), view.getListWidget());
			}

		});
	}

	public void newSortAction(SortBit sortBit) {
		logicHolder.getServiceFilterLinkedSortBit().sortOrderAction(sortBit);
	}

	@Override
	public void filterSearch() {
		searchSnips(formSearchOptionBean());
	}

	private AutoBean<SnipBean> formSearchOptionBean() {
		ServicesFilter filter = view.getServicesFilter();
		AutoBean<SnipBean> searchOptionsBean = view.getCurrentSearchOptionsBean();

		String authorText = filter.getAuthor().getText();
		if (!authorText.equals("")) {
			searchOptionsBean.as().setAuthor(authorText);
		} else {
			searchOptionsBean.as().setAuthor(null);
		}

		String categories = ViewUtils.getSelectedItems(filter.getCategoryList());
		if (!categories.equals("")) {
			searchOptionsBean.as().setCoreCat(categories);
		} else {
			searchOptionsBean.as().setCoreCat(null);
		}

		String posRefText = filter.getPosRef().getText();
		if (!posRefText.equals("")) {
			searchOptionsBean.as().setPosRef(Integer.parseInt(posRefText));
		} else {
			searchOptionsBean.as().setPosRef(null);
		}

		String neutralRefText = filter.getNeutralRef().getText();
		if (!neutralRefText.equals("")) {
			searchOptionsBean.as().setNeutralRef(Integer.parseInt(neutralRefText));
		} else {
			searchOptionsBean.as().setNeutralRef(null);
		}

		String negativeRefText = filter.getNegativeRef().getText();
		if (!negativeRefText.equals("")) {
			searchOptionsBean.as().setNegativeRef(Integer.parseInt(negativeRefText));
		} else {
			searchOptionsBean.as().setNegativeRef(null);
		}

		String dateFromText = filter.getDateFromTo().getDateFrom().getTextBox().getText();
		if (!dateFromText.equals("")) {
			searchOptionsBean.as().setDateFrom(dateFromText);
		} else {
			searchOptionsBean.as().setDateFrom(null);
		}

		String dateToText = filter.getDateFromTo().getDateTo().getTextBox().getText();
		if (!dateToText.equals("")) {
			searchOptionsBean.as().setDateTo(dateToText);
		} else {
			searchOptionsBean.as().setDateTo(null);
		}

		searchOptionsBean.as().setSortOrder(logicHolder.getServiceFilterLinkedSortBit().getSortOrder());
		searchOptionsBean.as().setSortField(logicHolder.getServiceFilterLinkedSortBit().getActiveSortParam());

		return searchOptionsBean;
	}

	private void initLinkedSortBit() {
		ServicesFilter filter = view.getServicesFilter();
		AutoBean<SnipBean> searchOptionsBean = view.getCurrentSearchOptionsBean();
		LinkedSortBit lsb = new LinkedSortBit();
		logicHolder.setServiceFilterLinkedSortBit(lsb);
		lsb.getLinkedList().add(new LinkedSortBit.Link(filter.getAuthorSortBit(), "author", 1));
		lsb.getLinkedList().add(new LinkedSortBit.Link(filter.getPosRefSortBit(), "posRef", 1));
		lsb.getLinkedList().add(new LinkedSortBit.Link(filter.getNeutrRefSortBit(), "neutralRef", 1));
		lsb.getLinkedList().add(new LinkedSortBit.Link(filter.getNegRefSortBit(), "negativeRef", 1));
		lsb.getLinkedList().add(new LinkedSortBit.Link(filter.getDateSortBit(), "creationDate", 1));
		lsb.setActiveSortParam(searchOptionsBean.as().getSortField());
		lsb.setSortOrder(lsb.getSortOrder());
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
