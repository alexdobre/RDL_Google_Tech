package com.therdl.client.view.impl;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.LoadingWidget;
import com.therdl.client.view.widget.SearchFilterWidget;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * SnipSearchViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI so user can search and view a list of snips
 *
 * @ SearchFilterWidget searchFilterWidget, a filter widget for the search
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ String token, a GWT History token see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html
 * @ AutoBean<CurrentUserBean> currentUserBean manages user state
 * @ AutoBean<SnipBean> currentSearchOptionsBean for autobeans see see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */
public class SnipSearchViewImpl extends AppMenuView implements SearchView {

	private static Logger log = Logger.getLogger("");

	private static SnipSearchViewImplUiBinder uiBinder = GWT.create(SnipSearchViewImplUiBinder.class);

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	interface SnipSearchViewImplUiBinder extends UiBinder<Widget, SnipSearchViewImpl> {
	}

	private Presenter presenter;

	SearchFilterWidget searchFilterWidget;

	@UiField
	FlowPanel snipSearchWidgetPanel;

	@UiField
	FlowPanel snipListRowContainer;
	ListWidget snipList;

	@UiField
	LoadingWidget loadingWidget;

	private AutoBean<CurrentUserBean> currentUserBean;

	private AutoBean<SnipBean> currentSearchOptionsBean;

	private Beanery beanery = GWT.create(Beanery.class);

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;

		searchFilterWidget = new SearchFilterWidget(this);
		snipSearchWidgetPanel.add(searchFilterWidget);
	}

	public AutoBean<CurrentUserBean> getCurrentUserBean() {
		return currentUserBean;
	}

	public AutoBean<SnipBean> getCurrentSearchOptionsBean() {
		return currentSearchOptionsBean;
	}

	public void setCurrentSearchOptionsBean(AutoBean<SnipBean> snipBean) {
		this.currentSearchOptionsBean = snipBean;
	}

	@Override
	public void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
		if (snipList == null) {
			snipList = new ListWidget(this, beanList, pageIndex);
			snipListRowContainer.add(snipList);
		} else {
			snipList.populateList(this, beanList);
		}
		this.currentSearchOptionsBean.as().setPageIndex(pageIndex);
		searchFilterWidget.populateSearchOptions();
		ViewUtils.hide(loadingWidget);
	}

	/**
	 * call presenter function to search snips for the given search options
	 */

	@Override
	public void doFilterSearch() {
		ViewUtils.show(loadingWidget);
		searchFilterWidget.setSearchFilterFields(currentSearchOptionsBean);
		presenter.searchSnips(currentSearchOptionsBean);
	}

	@Override
	public ListWidget getListWidget() {
		return snipList;
	}
}
