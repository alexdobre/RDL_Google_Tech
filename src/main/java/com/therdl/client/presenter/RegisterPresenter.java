package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.RegisterView;
import com.therdl.shared.Constants;
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

	public RegisterPresenter(RegisterView registerView, AppController appController) {
		super(appController);
		this.view = registerView;
		registerView.setPresenter(this);
	}

	/**
	 * standard runtime method for MVP architecture
	 *
	 * @param container       the view container
	 * @param currentUserBean the user state bean, mainly used for authorisation
	 *                   and to update the menu
	 */
	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		container.clear();
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
	 *                               password String password these credentilas will be used as identifiers for subsequent login
	 */


	@Override
	public void submitNewUser(AutoBean<AuthUserBean> bean) {
		log.info(AutoBeanCodex.encode(bean).getPayload());
		String updateUrl = GWT.getModuleBaseURL() + "getSession";

		log.info("RegisterPresenter submitNewUser with  updateUrl: " + updateUrl);

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		bean.as().setAction("signUp");
		String json = AutoBeanCodex.encode(bean).getPayload();

		try {

			requestBuilder.sendRequest(json, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {

					log.info("RegisterPresenter submitNewUser onResponseReceived json" + response.getText());
					// deserialise the bean
					AutoBean<AuthUserBean> bean = AutoBeanCodex.decode(beanery, AuthUserBean.class, response.getText());
					// on success user is authorised on sign up
					getController().setCurrentUserBean(bean.as().getName(), bean.as().getEmail(), true, bean.as().getToken());
					// return to welcome page
					History.newItem(RDLConstants.Tokens.WELCOME);

				}

				@Override
				public void onError(Request request, Throwable exception) {
					log.info("UpdateServiceImpl initialUpdate onError)" + exception.getLocalizedMessage());

				}

			});

		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}  // end try
	}
}
