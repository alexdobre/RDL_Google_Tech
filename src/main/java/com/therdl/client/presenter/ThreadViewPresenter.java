package com.therdl.client.presenter;

import com.therdl.client.app.AppController;
import com.therdl.client.view.SnipView;

/**
 * Presenter for the Forum module (Thread)
 */
public class ThreadViewPresenter extends SnipViewPresenter{
	public ThreadViewPresenter(SnipView snipView, AppController appController, String token) {
		super(snipView, appController, token);
	}
}
