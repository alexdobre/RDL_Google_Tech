package com.therdl.client.presenter;

import com.therdl.client.app.AppController;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.SnipView;

/**
 * Presenter for the Forum module (Thread)
 */
public class ThreadViewPresenter extends SnipViewPresenter{
	public ThreadViewPresenter(SnipView snipView, AppController appController, String token, ReplyRunt replyRunt) {
		super(snipView, appController, token, replyRunt);
	}

	@Override
	protected void showReplyIfAuthor (boolean isAuthor){
		//for forum an author may not leave reply at any time
		view.showHideReplyButton(true);
	}
}
