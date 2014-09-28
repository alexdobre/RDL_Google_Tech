package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.TribunalView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.client.view.widget.TribunalListRow;
import com.therdl.client.view.widget.TribunalSearchFilter;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The tribunal view
 */
public class TribunalViewImpl extends AbstractSnipSearch implements TribunalView {

	private static Logger log = Logger.getLogger(TribunalViewImpl.class.getName());

	private static TribunalViewImplUiBinder uiBinder = GWT.create(TribunalViewImplUiBinder.class);

	@Override
	public void setPresenter(TribunalView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public TribunalView.Presenter getPresenter() {
		return presenter;
	}

	interface TribunalViewImplUiBinder extends UiBinder<Widget, TribunalViewImpl> {
	}

	private TribunalView.Presenter presenter;
	private TribunalSearchFilter tribunalSearchFilter;


	public TribunalViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;
		tribunalSearchFilter = new TribunalSearchFilter(this);
		snipSearchWidgetPanel.add(tribunalSearchFilter);
	}

	@Override
	public void displaySnipList(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
		if (snipList == null) {
			snipList = new ListWidget(currentUserBean, beanList, pageIndex, new TribunalListRow(), presenter);
			snipListRowContainer.add(snipList);
		} else {
			snipList.populateList(currentUserBean, beanList);
		}
		this.currentSearchOptionsBean.as().setPageIndex(pageIndex);
		tribunalSearchFilter.populateSearchOptions();
		ViewUtils.hide(loadingWidget);
	}

	/**
	 * call presenter function to search snips for the given search options
	 */

	@Override
	public void doFilterSearch() {
		ViewUtils.show(loadingWidget);
		presenter.searchSnips(currentSearchOptionsBean);
	}
}
