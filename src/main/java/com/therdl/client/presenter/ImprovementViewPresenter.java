package com.therdl.client.presenter;

import com.therdl.client.app.AppController;
import com.therdl.client.view.SnipView;

/**
 * Presenter for the Improvements module (Improvement)
 */
public class ImprovementViewPresenter extends SnipViewPresenter{
	public ImprovementViewPresenter(SnipView snipView, AppController appController, String token) {
		super(snipView, appController, token);
	}

	@Override
	protected void showReplyIfAuthor (boolean isAuthor){
		//for improvements an author may not leave a reference to own improvement
		view.showHideReplyButton(!isAuthor);
	}
}