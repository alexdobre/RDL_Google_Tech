package com.therdl.client.view;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.RequestObserver;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * snip view triggered when snip row list widget is selected
 */
public interface SnipView extends RdlView {

	public interface Presenter {
		public void saveReference(AutoBean<SnipBean> bean);

		public void populateReplies(AutoBean<SnipBean> searchOptionsBean, final int pageIndex);

		public void giveSnipReputation(String id, final RequestObserver observer);

		public AppController getController();

	}

	void setPresenter(Presenter presenter);

	public Presenter getPresenter();

	public void viewSnip(AutoBean<SnipBean> snipBean);

	public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex, String listRange);

	public void giveRepResponseHandler();

	public void saveReferenceResponseHandler(String refType, String snipType);

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

	AppMenu getAppMenu();
}
