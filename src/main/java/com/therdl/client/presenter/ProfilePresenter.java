package com.therdl.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.RDL;
import com.therdl.client.app.AppController;
import com.therdl.client.presenter.runt.ProfileDescRunt;
import com.therdl.client.validation.UserViewValidator;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.common.ErrorCodeMapper;
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
	private ProfileDescRunt profileDescRunt;
	private AutoBean<CurrentUserBean> currentUserBean;

	public ProfilePresenter(ProfileView servicesView, AppController controller, ProfileDescRunt profileDescRunt) {
		super(controller);
		this.view = servicesView;
		this.profileDescRunt = profileDescRunt;
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
		checkLogin(null);
		this.currentUserBean = currentUserBean;
		container.clear();
		profileDescRunt.grabProfileDesc(view, currentUserBean);
		Log.info("Populate VIEW - BEGIN");
		view.populateView(currentUserBean);
		container.add(view.asWidget());
	}

	@Override
	public String changePassword(AutoBean<CurrentUserBean> currentUserBean, String oldPass, String newPass,
			String newPassConfirm) {
		String validRes = UserViewValidator.validateChangePassInput(oldPass, newPass, newPassConfirm);
		if (validRes != null)
			return validRes;
		submitChangePass(currentUserBean.as().getName(), currentUserBean.as().getToken(), oldPass, newPass,
				newPassConfirm);
		return null;
	}

	@Override
	public void updateProfile(String content) {
		profileDescRunt
				.updateProfileDesc(content, currentUserBean.as().getName(), currentUserBean.as().getToken(), view);
	}

	public void submitChangePass(String username, String token, String oldPass, String newPass, String newPassConfirm) {
		String submitUrl = GWT.getModuleBaseURL() + "getSession";
		Log.info("RegisterPresenter submitNewUser with  updateUrl: " + submitUrl);

		//populate the bean to send to server
		AutoBean<AuthUserBean> userBean = beanery.authBean();
		userBean.as().setName(username);
		userBean.as().setToken(token);
		userBean.as().setOldPass(oldPass);
		userBean.as().setPassword(newPass);
		userBean.as().setPasswordConfirm(newPassConfirm);

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(submitUrl));
		requestBuilder.setHeader("Content-Type", "application/json; charset=UTF-8");
		userBean.as().setAction("changePass");
		String json = AutoBeanCodex.encode(userBean).getPayload();
		try {

			requestBuilder.sendRequest(json, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					Log.info("ProfilePresenter submitChangePass onResponseReceived: " + response.getText());
					if (response.getText().equals("success")) {
						view.setFormSuccessMsg(RDL.getI18n().formSuccessPassChange());
					} else {
						view.setChangePassErrorMsg(ErrorCodeMapper.getI18nMessage(response.getText()));
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					Log.info("UpdateServiceImpl initialUpdate onError)" + exception.getLocalizedMessage());
					view.setChangePassErrorMsg(RDL.getI18n().serverErrorC000());
				}
			});

		} catch (RequestException e) {
			Log.info(e.getLocalizedMessage());
		}
	}
}
