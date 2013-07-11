package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.therdl.client.dto.SnipSearchProxy;
import com.therdl.client.view.SnipSearchView;

import java.util.logging.Logger;

public class SnipSearchPresenter implements Presenter, SnipSearchView.Presenter<SnipSearchProxy> {
    private static Logger log = Logger.getLogger("");
	private final SnipSearchView<SnipSearchProxy> snipSearchView;
	
	public SnipSearchPresenter(SnipSearchView<SnipSearchProxy> snipSearchView){
		this.snipSearchView = snipSearchView;
        log.info("SnipSearchPresenter constructor");

	}
	
	@Override
	public void go(HasWidgets container) {
        log.info("SnipSearchPresenter go adding view");
		container.clear();
	    container.add(snipSearchView.asWidget());		
	}

}
