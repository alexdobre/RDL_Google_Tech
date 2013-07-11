package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.therdl.client.view.WelcomeView;


public class WelcomePresenter implements Presenter, WelcomeView.Presenter {

	private final WelcomeView welcomeView;
	
	public WelcomePresenter(WelcomeView welcomeView){
		this.welcomeView = welcomeView;
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
	    container.add(welcomeView.asWidget());		
	}

	

}
