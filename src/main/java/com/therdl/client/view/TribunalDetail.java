package com.therdl.client.view;

import java.util.ArrayList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.presenter.PaginationPresenter;
import com.therdl.shared.beans.SnipBean;

/**
 * User views the details of a tribunal item
 */
public interface TribunalDetail extends RdlView, PaginatedView, ValidatedView, SnipView {

	public interface Presenter extends PaginationPresenter {
		public void saveComment(AutoBean<SnipBean> bean);

		public void populateReplies(AutoBean<SnipBean> searchOptionsBean);

		public void giveSnipReputation(String id, final RequestObserver observer);
	}

	void setPresenter(Presenter presenter);

	public TribunalDetail.Presenter getTribunalPresenter();

	public void populateDetails(AutoBean<SnipBean> snipBean);

	public void showComments(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

	public void showHideReplyButton(Boolean show);

}
