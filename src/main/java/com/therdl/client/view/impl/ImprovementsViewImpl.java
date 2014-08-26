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
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * ImprovementsViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the RDL user created and voted features
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * <p/>
 * fields below are standard GWT UIBinder display elements
 * @ AutoBean<CurrentUserBean> currentUser  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * maintains client side state
 */
public class ImprovementsViewImpl extends AppMenuView implements SearchView {

	private static Logger log = Logger.getLogger("");

	private static ImprovementsViewImplUiBinder uiBinder = GWT.create(ImprovementsViewImplUiBinder.class);

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	interface ImprovementsViewImplUiBinder extends UiBinder<Widget, ImprovementsViewImpl> {
	}

	private Presenter presenter;

	SearchFilterWidget searchFilterWidget;

	@UiField
	FlowPanel impSearchWidgetPanel;

	@UiField
	FlowPanel impListRowContainer;

	ListWidget improvementsList;

	@UiField
	LoadingWidget impLoadingWidget;

	private AutoBean<CurrentUserBean> currentUserBean;

	private AutoBean<SnipBean> currentSearchOptionsBean;

	public ImprovementsViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;

		searchFilterWidget = new SearchFilterWidget(this);
		impSearchWidgetPanel.add(searchFilterWidget);
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
		if (improvementsList == null) {
			improvementsList = new ListWidget(this, beanList, pageIndex);
			impListRowContainer.add(improvementsList);
		} else {
			improvementsList.populateList(this, beanList);
		}
		this.currentSearchOptionsBean.as().setPageIndex(pageIndex);
		searchFilterWidget.populateSearchOptions();
		this.getListWidget().setPageIndex(pageIndex);
		ViewUtils.hide(impLoadingWidget);
	}

	/**
	 * call presenter function to search snips for the given search options
	 */
	@Override
	public void doFilterSearch() {
		ViewUtils.show(impLoadingWidget);
		searchFilterWidget.setSearchFilterFields(currentSearchOptionsBean);
		this.getListWidget().setPageIndex(currentSearchOptionsBean.as().getPageIndex());
		presenter.searchSnips(currentSearchOptionsBean);
	}

	@Override
	public ListWidget getListWidget() {
		return improvementsList;
	}

}
