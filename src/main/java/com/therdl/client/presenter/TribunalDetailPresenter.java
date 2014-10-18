package com.therdl.client.presenter;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.TribunalDetail;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * User views a tribunal item details
 */
public class TribunalDetailPresenter extends SnipViewPresenter implements TribunalDetail.Presenter {

	private static Logger log = Logger.getLogger(TribunalDetailPresenter.class.getName());

	public static final String VOTE_YES="YES";
	public static final String VOTE_NO="NO";

	public TribunalDetailPresenter(TribunalDetail tribunalDetail, AppController appController, String token) {
		super(tribunalDetail, appController, token);
		tribunalDetail.setTribunalPresenter(this);
	}

	@Override
	protected void showReplyIfAuthor(boolean isAuthor) {
		view.showHideReplyButton(true);
	}

	@Override
	public void saveComment(AutoBean<SnipBean> bean) {
		super.saveReference(bean);
	}

	@Override
	public void abuseVoteDisplayLogic(Button yesVoteAbuse, Button noVoteAbuse,
	                                  Paragraph yesVotedAbuse, Paragraph noVotedAbuse) {
		boolean display = true;
		boolean votedYes = false;
		boolean votedNo = false;
		//user must be logged in
		if (currentUserBean == null || !currentUserBean.as().isAuth()) {
			display = false;
		//user must be allowed to vote = isAllowedAbuseAction
		} else if (ViewUtils.isAllowedAbuseAction(currentUserBean)) {
			display = false;
		//user must not have voted already (test if voted YES)
		} else if (currentSnipBean.as().getIsAbuseReportedByUser() == 1) {
			display = false;
			votedYes = true;
			//user must not have voted already (test if voted NO)
		} else if (currentSnipBean.as().getIsAbuseReportedNoByUser() == 1) {
			display = false;
			votedNo = true;
		}

		ViewUtils.showHide(display, yesVoteAbuse);
		ViewUtils.showHide(display, noVoteAbuse);
		ViewUtils.showHide(votedYes, yesVotedAbuse);
		ViewUtils.showHide(votedNo, noVotedAbuse);
	}

	@Override
	public void abuseVote(final String vote, Button yesVoteAbuse, Button noVoteAbuse,
	                      Paragraph yesVotedAbuse, Paragraph noVotedAbuse) {

		log.info("Vote abuse -BEGIN voted:  "+vote+" on item: "+currentSnipBean.as().getId());
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		AutoBean<SnipBean> actionBean = beanery.snipBean();
		actionBean.as().setAction("reportAbuse");
		actionBean.as().setParentSnip(currentSnipBean.as().getId());
		actionBean.as().setContent(null);
		actionBean.as().setAuthor(getController().getCurrentUserBean().as().getName());
		actionBean.as().setToken(getController().getCurrentUserBean().as().getToken());
		if (VOTE_YES.equals(vote)) {
			actionBean.as().setIsAbuseReportedByUser(1);
		} else {
			actionBean.as().setIsAbuseReportedNoByUser(1);
		}

		String json = AutoBeanCodex.encode(actionBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new StatusCallback(null) {
				@Override
				public void onSuccess(Request request, Response response) {
					log.info("vote abuse on success");
					if (VOTE_YES.equals(vote)) {
						currentSnipBean.as().setIsAbuseReportedByUser(1);
						currentSnipBean.as().setAbuseCount(currentSnipBean.as().getAbuseCount() + 1);
					} else {
						currentSnipBean.as().setIsAbuseReportedNoByUser(1);
						currentSnipBean.as().setNoAbuseCount(currentSnipBean.as().getNoAbuseCount() + 1);
					}
					view.populateSnip(currentSnipBean);
				}
			});
		} catch (RequestException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void populateReplies(AutoBean<SnipBean> searchOptionsBean) {
		log.info("Retrieve abuse comments for: " + searchOptionsBean.as());
		String updateUrl = GWT.getModuleBaseURL() + RDLConstants.SnipAction.SNIP_SERVLET_URL;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		formRepliesSearchOptions(searchOptionsBean);

		final int pageIndex = searchOptionsBean.as().getPageIndex();

		String json = AutoBeanCodex.encode(searchOptionsBean).getPayload();
		try {
			requestBuilder.sendRequest(json, new SnipListCallback() {

				public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
					view.showReferences(beanList, pageIndex);
					PaginationHelper.showPaginationOnView(pageIndex, beanList.size(), view);
				}

			});
		} catch (RequestException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void formRepliesSearchOptions (AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setAction("searchAbuse");
		searchOptionsBean.as().setToken(getController().getCurrentUserBean().as().getToken());
		searchOptionsBean.as().setPageIndex(0);
		searchOptionsBean.as().setSortOrder(1);
		searchOptionsBean.as().setSortField("creationDate");
		searchOptionsBean.as().setReturnSnipContent(true);
	}

	@Override
	protected void snipActionLogic(AutoBean<SnipBean> returnedBean) {
		//noop
	}
}
