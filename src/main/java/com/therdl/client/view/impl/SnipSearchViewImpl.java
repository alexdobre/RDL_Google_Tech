package com.therdl.client.view.impl;

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
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.logging.Logger;

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

	private String token;

	private String authorName;

	private boolean firstTimeLoaded = false;

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;

		searchFilterWidget = new SearchFilterWidget(this);
		snipSearchWidgetPanel.add(searchFilterWidget);
	}

	public void setToken(String token) {

		String[] tokenSplit = token.split(":");
		if (tokenSplit.length == 2) {
			this.token = tokenSplit[0];
			this.authorName = tokenSplit[1];
		} else {
			this.token = token;
			this.authorName = null;
		}
		appMenu.setIdeasActive();
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		if (token.equals(RDLConstants.Tokens.SNIPS)) {
			if (authorName != null) {
				AutoBean<SnipBean> searchOptionsBean = initSearchOptionsBean();
				searchOptionsBean.as().setAuthor(authorName);

				doFilterSearch(searchOptionsBean, 0);
			} else {
				if (!firstTimeLoaded)
					getInitialSnipList(0);
			}
		} else {
			AutoBean<SnipBean> snipBean = ViewUtils.parseToken(beanery, token);
			doFilterSearch(snipBean, 0);
		}

	}

	public AutoBean<SnipBean> initSearchOptionsBean() {
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSnipType(RDLConstants.SnipType.SNIP + "," + RDLConstants.SnipType.HABIT + "," + RDLConstants.SnipType.FAST_CAP + "," + RDLConstants.SnipType.MATERIAL);

		return searchOptionsBean;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public AutoBean<CurrentUserBean> getCurrentUserBean() {
		return currentUserBean;
	}

	public AutoBean<SnipBean> getCurrentSearchOptionsBean() {
		return currentSearchOptionsBean;
	}

	@Override
	public void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex, String listRange) {
		authorName = null;
		if (snipList == null){
			snipList = new ListWidget(this, beanList, pageIndex, listRange, presenter.getPaginationPresenter());
			snipListRowContainer.add(snipList);
		}else{
			snipList.populateList(this,beanList,listRange);
		}

		ViewUtils.hide(loadingWidget);
	}

	/**
	 * call presenter function to search snips for the given search options
	 *
	 * @param searchOptionsBean bean for the search options
	 * @param pageIndex
	 */

	@Override
	public void doFilterSearch(AutoBean<SnipBean> searchOptionsBean, int pageIndex) {
		ViewUtils.show(loadingWidget);
		currentSearchOptionsBean = searchOptionsBean;
		searchFilterWidget.setSearchFilterFields(currentSearchOptionsBean);
		presenter.searchSnips(searchOptionsBean, pageIndex);
	}

	/**
	 * call presenter function to retrieve initial list for snips
	 */
	@Override
	public void getInitialSnipList(int pageIndex) {
		firstTimeLoaded = true;
		currentSearchOptionsBean = null;
		ViewUtils.show(loadingWidget);

		presenter.searchSnips(initSearchOptionsBean(), pageIndex);
	}

	@Override
	public ListWidget getListWidget(){
		return snipList;
	}

	@Override
	public SearchFilterWidget getFilterWidget(){
		return searchFilterWidget;
	}
}
