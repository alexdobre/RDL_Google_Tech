package com.therdl.client.presenter;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.TribunalDetail;
import com.therdl.shared.beans.SnipBean;

/**
 * User views a tribunal item details
 */
public class TribunalDetailPresenter extends SnipViewPresenter implements TribunalDetail.Presenter {

	public TribunalDetailPresenter(TribunalDetail tribunalDetail, AppController appController, String token) {
		super(tribunalDetail, appController, token);
	}

	@Override
	protected void showReplyIfAuthor(boolean isAuthor) {
		view.showHideReplyButton(true);
	}

	@Override
	public void saveComment(AutoBean<SnipBean> bean) {
		super.saveReference(bean);
	}
}
