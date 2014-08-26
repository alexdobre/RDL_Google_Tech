package com.therdl.client.view;

import org.gwtbootstrap3.client.ui.Modal;

/**
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ doForgotPassword(String email) submit user email for getting a new password if the email is valid.
 */
public interface ForgotPassword extends RdlView, ValidatedView {

	public interface Presenter {
		String doForgotPassword(String email, String username);

		void showForgotPasswordPopup();
	}

	void setPresenter(Presenter presenter);

	Modal getForgotPassModal();
}
