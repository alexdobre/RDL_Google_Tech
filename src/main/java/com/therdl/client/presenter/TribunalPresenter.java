package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.TribunalView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Logic behind the tribunal view
 */
public class TribunalPresenter extends RdlAbstractPresenter<TribunalView> implements TribunalView.Presenter {

	public TribunalPresenter(TribunalView tribunalView, AppController controller) {
		super(controller);
		this.view = tribunalView;
		this.view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		log.info("SnipSearchPresenter go adding view");
		checkLogin();
		container.clear();
		view.getAppMenu().setActive();
		container.add(view.asWidget());
	}
}
