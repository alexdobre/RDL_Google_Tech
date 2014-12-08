package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.ContentNotFound;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Presenter for the content not found page
 */
public class ContentNotFoundPresenter extends RdlAbstractPresenter<ContentNotFound> implements ContentNotFound.Presenter {

	public ContentNotFoundPresenter(ContentNotFound contentNotFound, AppController controller) {
		super(controller);
		this.view = contentNotFound;
	}


	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin(null);
		container.clear();
		container.add(view.asWidget());
	}
}
