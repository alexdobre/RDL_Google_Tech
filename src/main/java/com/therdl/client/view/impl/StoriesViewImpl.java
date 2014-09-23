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
import com.therdl.client.view.widget.SnipListRow;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * StoriesViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the RDL forum implementation
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * fields below are standard GWT UIBinder display elements
 * @ AutoBean<CurrentUserBean> currentUser  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * maintains client side state
 */
public class StoriesViewImpl extends AppMenuView implements SearchView {

	private static Logger log = Logger.getLogger("StoriesViewImpl");

	private static StoriesViewImplUiBinder uiBinder = GWT.create(StoriesViewImplUiBinder.class);

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	interface StoriesViewImplUiBinder extends UiBinder<Widget, StoriesViewImpl> {
	}

	private Presenter presenter;

	SearchFilterWidget searchFilterWidget;

	@UiField
	FlowPanel threadSearchWidgetPanel;

	@UiField
	FlowPanel threadListRowContainer;

	@UiField
	LoadingWidget threadLoadingWidget;

	ListWidget storiesList;

	private AutoBean<CurrentUserBean> currentUserBean;

	private AutoBean<SnipBean> currentSearchOptionsBean;

	private Beanery beanery = GWT.create(Beanery.class);

	public StoriesViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		this.currentUserBean = currentUserBean;
		searchFilterWidget = new SearchFilterWidget(this);

		initWidget(uiBinder.createAndBindUi(this));
		threadSearchWidgetPanel.add(searchFilterWidget);
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
		log.info("On display snip list storiesList=" + storiesList);
		if (storiesList == null) {
			storiesList = new ListWidget(this, beanList, pageIndex, new SnipListRow());
			threadListRowContainer.add(storiesList);
		} else {
			storiesList.populateList(this, beanList);
		}
		this.currentSearchOptionsBean.as().setPageIndex(pageIndex);

		searchFilterWidget.populateSearchOptions();
		ViewUtils.hide(threadLoadingWidget);
	}

	/**
	 * call presenter function to search snips for the given search options
	 */
	@Override
	public void doFilterSearch() {
		ViewUtils.show(threadLoadingWidget);
		searchFilterWidget.setSearchFilterFields(currentSearchOptionsBean);
		presenter.searchSnips(currentSearchOptionsBean);
	}

	@Override
	public ListWidget getListWidget() {
		return storiesList;
	}
}
