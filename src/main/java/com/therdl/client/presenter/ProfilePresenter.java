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
import com.therdl.client.RDL;
import com.therdl.client.app.AppController;
import com.therdl.client.view.ProfileView;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * ProfilePresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the profile related data in and out of the client
 * for example the User's Avatar upload and subsequent image presentation
 *
 * @ ProfileView  profileView this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */
public class ProfilePresenter extends RdlAbstractPresenter<ProfileView> implements ProfileView.Presenter {

	private Beanery beanery = GWT.create(Beanery.class);

	public ProfilePresenter(ProfileView servicesView, AppController controller) {
		super(controller);
		this.view = servicesView;
		servicesView.setPresenter(this);
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
		checkLogin();
		container.clear();
		view.populateView(currentUserBean);
		container.add(view.asWidget());
	}


	@Override
	public String changePassword(AutoBean<CurrentUserBean> currentUserBean, String oldPass, String newPass, String newPassConfirm) {
		String validRes = validateChangePassInput(oldPass, newPass, newPassConfirm);
		if (validRes != null ) return validRes;

		submitChangePass(currentUserBean.as().getName(), currentUserBean.as().getToken(), oldPass, newPass, newPassConfirm);
		return null;
	}

	private String validateChangePassInput(String oldPass, String newPass, String newPassConfirm){
		//user input cannot be empty
		if (RDLUtils.isEmpty(oldPass) || RDLUtils.isEmpty(newPass) || RDLUtils.isEmpty(newPassConfirm)){
			return RDL.getI18n().formErrorPleaseProvideInput();
		}

		if (!newPass.equals(newPassConfirm)){
			return RDL.getI18n().formErrorPassEqual();
		}

		return null;
	}

	public void submitChangePass (String username, String token, String oldPass, String newPass, String newPassConfirm) {
		String submitUrl = GWT.getModuleBaseURL() + "getSession";
		// handle jboss urls for deploy
		if (!Constants.DEPLOY) {
			submitUrl = submitUrl.replaceAll("/therdl", "");
		}
		log.info("RegisterPresenter submitNewUser with  updateUrl: " + submitUrl);

		//populate the bean to send to server
		AutoBean<AuthUserBean> userBean = beanery.authBean();
		userBean.as().setName(username);
		userBean.as().setToken(token);
		userBean.as().setOldPass(oldPass);
		userBean.as().setPassword(newPass);
		userBean.as().setPasswordConfirm(newPassConfirm);

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(submitUrl));
		requestBuilder.setHeader("Content-Type", "application/json");
		userBean.as().setAction("changePass");
		String json = AutoBeanCodex.encode(userBean).getPayload();

		try {

			requestBuilder.sendRequest(json, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					log.info("ProfilePresenter submitChangePass onResponseReceived json" + response.getText());
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
