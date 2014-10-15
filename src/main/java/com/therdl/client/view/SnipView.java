package com.therdl.client.view;

import java.util.ArrayList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.handler.ClickHandler;
import com.therdl.client.presenter.PaginationPresenter;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.widget.ReferenceSearchFilterWidget;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.view.widget.SnipActionWidget;
import com.therdl.shared.beans.SnipBean;

/**
 * snip view triggered when snip row list widget is selected
 */
public interface SnipView extends RdlView, PaginatedView, ValidatedView {

	public interface Presenter extends PaginationPresenter{
		public void saveReference(AutoBean<SnipBean> bean);

		public void populateReplies(AutoBean<SnipBean> searchOptionsBean);
	}

	void setPresenter(Presenter presenter);

	public Presenter getPresenter();

	public void populateSnip(AutoBean<SnipBean> snipBean);

	public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

	public void saveReferenceResponseHandler(String refType, String snipType);

	public ReferenceSearchFilterWidget getFilter();

	public SnipActionWidget getSnipActionWidget();

	public void showHideReplyButton(Boolean show);

	public AutoBean<SnipBean> getSearchOptionsBean();

	public void setSearchOptionsBean(AutoBean<SnipBean> searchOptionsBean);

}
