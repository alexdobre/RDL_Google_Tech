package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.FaqView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The FAQ presenter
 */
public class FaqPresenter extends RdlAbstractPresenter<FaqView> implements FaqView.Presenter {

	private AutoBean<CurrentUserBean> currentUserBean;

	public FaqPresenter(FaqView faqView, AppController controller) {
		super(controller);
		this.view = faqView;
		this.view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		this.currentUserBean = currentUserBean;
		container.clear();

		//TODO grab the FAQ list from the DB
		container.add(view.asWidget());
		view.getAppMenu().setActive();
	}

}
