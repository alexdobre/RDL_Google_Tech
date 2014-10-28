package com.therdl.client.view;


import com.google.gwt.user.client.ui.Panel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;

/**
 * see com.therdl.client.view.impl.RegisterViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 */
public interface RegisterView extends RdlView, ValidatedView {


	public interface Presenter {
		void submitNewUser(AutoBean<AuthUserBean> bean);
	}

	void setPresenter(Presenter presenter);

	public Panel getSignUpMessage();
}
