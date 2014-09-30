package com.therdl.client.presenter;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.view.TribunalDetail;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.shared.beans.SnipBean;

/**
 * User views a tribunal item details
 */
public class TribunalDetailPresenter extends SnipViewPresenter implements TribunalDetail.Presenter {

	private static Logger log = Logger.getLogger(TribunalDetailPresenter.class.getName());

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
	public void populateReplies(AutoBean<SnipBean> searchOptionsBean) {
		log.info("Retrieve abuse comments for: " + searchOptionsBean.as());
		String updateUrl = GWT.getModuleBaseURL() + "getSnips";
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
}
