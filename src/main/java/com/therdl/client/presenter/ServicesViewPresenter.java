package com.therdl.client.presenter;

import com.therdl.client.app.AppController;
import com.therdl.client.view.ServiceView;

/**
 * Presenter for the services module
 */
public class ServicesViewPresenter extends SnipViewPresenter {

	public ServicesViewPresenter(ServiceView serviceView, AppController appController, String token) {
		super(serviceView, appController, token);
	}

	@Override
	protected void showReplyIfAuthor(boolean isAuthor) {
		//for services an author may not leave a reference to own snip
		//they also may not leave a reference if they already replied
		Boolean refGiven = false;
		if (currentSnipBean != null && currentSnipBean.as().getIsRefGivenByUser() != null) {
			refGiven = currentSnipBean.as().getIsRefGivenByUser() == 1;
		}
		view.showHideReplyButton(!isAuthor && !refGiven);
	}

}
