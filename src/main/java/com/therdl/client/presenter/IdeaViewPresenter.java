package com.therdl.client.presenter;

import com.therdl.client.app.AppController;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.SnipView;

/**
 * Presenter for the Ideas module (Snip, Material, FastCap, Habit)
 */
public class IdeaViewPresenter extends SnipViewPresenter{

	public IdeaViewPresenter(SnipView snipView, AppController appController, String token, ReplyRunt replyRunt) {
		super(snipView, appController, token, replyRunt);
	}

	@Override
	protected void showReplyIfAuthor (boolean isAuthor){
		//for ideas an author may not leave a reference to own snip
		//they also may not leave a reference if they already replied
		Boolean refGiven = currentSnipBean.as().getIsRefGivenByUser() == 1;
		view.showHideReplyButton(!isAuthor && !refGiven);
	}

}
