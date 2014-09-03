package com.therdl.client.presenter;

import com.therdl.client.app.AppController;
import com.therdl.client.view.SnipView;

/**
 * Presenter for the Ideas module (Snip, Material, FastCap, Habit)
 */
public class IdeaViewPresenter extends SnipViewPresenter{

	public IdeaViewPresenter(SnipView snipView, AppController appController, String token) {
		super(snipView, appController, token);
	}

	@Override
	protected void showReplyIfAuthor (boolean isAuthor){
		//for ideas an author may not leave a reference to own snip
		view.showHideReplyButton(!isAuthor);
	}

}