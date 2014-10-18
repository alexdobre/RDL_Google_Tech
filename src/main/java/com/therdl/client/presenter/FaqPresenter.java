package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.app.FuncFactory;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.view.FaqView;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * The FAQ presenter
 */
public class FaqPresenter extends RdlAbstractPresenter<FaqView> implements FaqView.Presenter {

	private AutoBean<CurrentUserBean> currentUserBean;
	private GrabSnipFunc grabSnipFunc;

	public FaqPresenter(FaqView faqView, AppController controller) {
		super(controller);
		this.view = faqView;
		this.view.setPresenter(this);
		this.grabSnipFunc = FuncFactory.createGrabSnipFunc();
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		this.currentUserBean = currentUserBean;
		container.clear();

		if (!view.isPopulated()) {
			grabSnipFunc.grabFaqList(new SnipListCallback() {
				@Override
				public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
					view.populateFaq(beanList);
				}
			});
		}

		container.add(view.asWidget());
		view.getAppMenu().setActive();
	}

}
