package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.SearchFilterWidget;
import com.therdl.client.view.widget.SnipListRow;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

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
public class SnipSearchViewImpl extends AbstractSnipSearch implements SearchView {

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
	private SearchFilterWidget searchFilterWidget;

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;

		searchFilterWidget = new SearchFilterWidget(this);
		snipSearchWidgetPanel.add(searchFilterWidget);
	}

	@Override
	public void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
		if (snipList == null) {
			snipList = new ListWidget(currentUserBean, beanList, pageIndex, new SnipListRow(), presenter);
			snipListRowContainer.add(snipList);
		} else {
			snipList.populateList(currentUserBean, beanList);
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

}
