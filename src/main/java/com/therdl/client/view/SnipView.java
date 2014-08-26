package com.therdl.client.view;

import java.util.ArrayList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.widget.ReferenceSearchFilterWidget;
import com.therdl.shared.RequestObserver;
import com.therdl.shared.beans.SnipBean;

/**
 * snip view triggered when snip row list widget is selected
 */
public interface SnipView extends RdlView, PaginatedView {

	public interface Presenter {
		public void saveReference(AutoBean<SnipBean> bean);

		public void populateReplies(AutoBean<SnipBean> searchOptionsBean);

		public void giveSnipReputation(String id, final RequestObserver observer);

		public AppController getController();

	}

	void setPresenter(Presenter presenter);

	public Presenter getPresenter();

	public void viewSnip(AutoBean<SnipBean> snipBean);

	public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

	public void giveRepResponseHandler();

	public void saveReferenceResponseHandler(String refType, String snipType);

	public ReferenceSearchFilterWidget getFilter();

	/**
	 * In the bottom right corner of the view snip panel we can have one of three options
	 * 1- user logged in and is author = edit button
	 * 2- user logged in and rep already given = rep given icon
	 * 3- user logged in and rep not given or user not logged in = like button
	 *
	 * @param isAuthor user is author (false if not logged in)
	 * @param repGiven user already gave rep
	 */
	public void showHideLikeOrEditButton(Boolean isAuthor, Boolean repGiven);

	public void showHideReplyButton(Boolean show);

	public AutoBean<SnipBean> getSearchOptionsBean();

	public void setSearchOptionsBean(AutoBean<SnipBean> searchOptionsBean);

}
