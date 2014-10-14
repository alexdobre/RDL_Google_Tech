package com.therdl.client.view;

import java.util.ArrayList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.handler.ClickHandler;
import com.therdl.client.presenter.PaginationPresenter;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.widget.ReferenceSearchFilterWidget;
import com.therdl.client.handler.RequestObserver;
import com.therdl.shared.beans.SnipBean;

/**
 * snip view triggered when snip row list widget is selected
 */
public interface SnipView extends RdlView, PaginatedView, ValidatedView {

	public interface Presenter extends PaginationPresenter{
		public void saveReference(AutoBean<SnipBean> bean);

		public void populateReplies(AutoBean<SnipBean> searchOptionsBean);

		public void giveSnipReputation(String id, final RequestObserver observer);

	}

	void setPresenter(Presenter presenter);

	public Presenter getPresenter();

	public void populateSnip(AutoBean<SnipBean> snipBean);

	public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex, ReplyRunt replyRunt);

	public void saveReferenceResponseHandler(String refType, String snipType);

	public ReferenceSearchFilterWidget getFilter();

	/**
	 * Show the snip action
	 * @param isEdit true if show edit button, false if show give rep, null if show rep given
	 * @param clickHandler the click handler to be invoked on user click
	 */
	public void showSnipAction(Boolean isEdit, ClickHandler clickHandler);

	public void hideSnipAction();

	public void showHideReplyButton(Boolean show);

	public AutoBean<SnipBean> getSearchOptionsBean();

	public void setSearchOptionsBean(AutoBean<SnipBean> searchOptionsBean);

}
