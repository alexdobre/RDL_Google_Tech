package com.therdl.client.view;

import java.util.ArrayList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.presenter.PaginationPresenter;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * User views the details of a tribunal item
 */
public interface TribunalDetail extends RdlView, PaginatedView, ValidatedView, SnipView {

	public interface Presenter extends PaginationPresenter {
		public void saveComment(AutoBean<SnipBean> bean);

		public void populateReplies(AutoBean<SnipBean> searchOptionsBean);

		/**
		 * Decides if the vote abuse buttons are to be displayed
		 * @param yesVoteAbuse yes button
		 * @param noVoteAbuse no button
		 * @param yesVotedAbuse voted yes paragraph
		 * @param noVotedAbuse voted no paragraph
		 */
		public void abuseVoteDisplayLogic(Button yesVoteAbuse, Button noVoteAbuse,
		                                  Paragraph yesVotedAbuse, Paragraph noVotedAbuse);

		/**
		 * Executes the vote
		 * @param vote the string YES or NO
		 * @param yesVoteAbuse yes button
		 * @param noVoteAbuse no button
		 * @param yesVotedAbuse voted yes paragraph
		 * @param noVotedAbuse voted no paragraph
		 */
		public void abuseVote(String vote, Button yesVoteAbuse, Button noVoteAbuse,
		                      Paragraph yesVotedAbuse, Paragraph noVotedAbuse);
	}

	void setPresenter(Presenter presenter);

	public TribunalDetail.Presenter getTribunalPresenter();
	public void setTribunalPresenter (TribunalDetail.Presenter presenter);

	public void populateDetails(AutoBean<SnipBean> snipBean);

	public void showComments(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex);

	public void showHideReplyButton(Boolean show);

}
