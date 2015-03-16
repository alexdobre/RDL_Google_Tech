package com.therdl.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.RDL;
import com.therdl.client.app.AppController;
import com.therdl.client.app.RuntFactory;
import com.therdl.client.callback.BeanCallback;
import com.therdl.client.presenter.runt.ContentMgmtRunt;
import com.therdl.client.validation.UserViewValidator;
import com.therdl.client.view.RegisterView;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * RegisterPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 *
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ RegisterView  registerView this presenter GUI component
 * @ void submitNewUser(AutoBean<AuthUserBean> bean) user signup to <URI base path>/getSession
 * calls com.therdl.server.restapi.SessionServlet class and updates the view depending on given/allowed
 * authorisation in server callback onResponseReceived(Request request, Response response)
 */
public class RegisterPresenter extends RdlAbstractPresenter<RegisterView> implements RegisterView.Presenter {

	private ContentMgmtRunt contentMgmtRunt;

	public RegisterPresenter(RegisterView registerView, AppController appController) {
		super(appController);
		this.view = registerView;
		contentMgmtRunt = RuntFactory.createContentMgmgtRunt();
		registerView.setPresenter(this);
	}

	/**
	 * standard runtime method for MVP architecture
	 *
	 * @param container       the view container
	 * @param currentUserBean the user state bean, mainly used for authorisation
	 *                        and to update the menu
	 */
	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin(null);
		container.clear();
		view.hideMessages();
		contentMgmtRunt.displaySignInMsg(view.getSignUpMessage());
		container.add(view.asWidget());
	}

	/**
	 * calls com.therdl.server.restapi.SessionServlet class to authorise user from database, creates
	 * a AutoBean<AuthUserBean> authBean from the users supplied credentials and submits it as a json serialised object
	 * calls AppController controller and  WelcomeView  welcomeView objects
	 * controller.setCurrentUserBean(name, email,  auth)::  sets the authorisation state for a newly signed up
	 * user for the upper menu in the WelcomeView
	 *
	 * @param bean) constructed from the submitted email String (unique identifier) and
	 *              password String password these credentilas will be used as identifiers for subsequent login
	 */

	@Override
	public void submitNewUser(AutoBean<AuthUserBean> bean) {
		String validationResult = UserViewValidator.validateAuthUserBean(bean);
		if (validationResult != null) {
			view.setErrorMessage(validationResult);
			return;
		}

		String updateUrl = GWT.getModuleBaseURL() + "getSession";
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json; charset=UTF-8");
		bean.as().setAction("signUp");
		String json = AutoBeanCodex.encode(bean).getPayload();

		try {
			requestBuilder.sendRequest(json, new BeanCallback<AuthUserBean>(AuthUserBean.class, view) {
				@Override
				public void onBeanReturned(AutoBean<AuthUserBean> returnedBean) {
					// on success user is authorised on sign up
					getController().setCurrentUserBean(returnedBean.as().getName(), returnedBean.as().getEmail(),
							true, returnedBean.as().getToken());
					view.setSuccessMessage(RDL.getI18n().formSuccessGeneric());
					// return to welcome page
					History.newItem(RDLConstants.Tokens.WELCOME);
				}
			});

		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
	}

}
