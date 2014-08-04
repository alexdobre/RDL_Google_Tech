package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.RegisterView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.FieldVerifier;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;

import java.util.logging.Logger;

/**
 * RegisterViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI so user can Registers/signs-up
 *
 * @ RegisterView.Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * fields below are standard GWT form fields for user sign-up
 * @ TextBox userName, email
 * @ PasswordTextBox psswd , cpsswd =='check password abbreviation for clarity'
 * @ Button submitBtn
 */
public class RegisterViewImpl extends AppMenuView implements RegisterView {

	private static Logger log = Logger.getLogger("");

	interface RegisterViewImplUiBinder extends UiBinder<Widget, RegisterViewImpl> {
	}

	private static RegisterViewImplUiBinder uiBinder = GWT.create(RegisterViewImplUiBinder.class);

	private RegisterView.Presenter presenter;

	private Beanery beanery = GWT.create(Beanery.class);

	@UiField
	TextBox userName;

	@UiField
	TextBox email;

	@UiField
	PasswordTextBox psswd;

	@UiField
	PasswordTextBox cpsswd;

	@UiField
	org.gwtbootstrap3.client.ui.Button submitBtn;

	private String username;
	private String password;
	private String eMail;

	public RegisterViewImpl(AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		appMenu.setSignUpView();
		appMenu.setSignUpActive();
	}


	/**
	 * Handler for form submit
	 *
	 * @param event ClickEvent Standard GWT ClickEvent
	 *              FieldVerifier static class for validation
	 *              AutoBean<AuthUserBean> newUserBean construct a bgean from supplied credentials
	 *              presenter.submitNewUser(newUserBean) submits bean for sign up in com.therdl.server.restapi.SessionServlet class
	 */
	@UiHandler("submitBtn")
	public void onSubmit(ClickEvent event) {
		log.info("RegisterViewImpl onSubmit verifying fields");

		username = userName.getText();
		password = psswd.getText();
		eMail = email.getText();

		log.info("RegisterViewImpl onSubmit verifying fields email.getText() " + email.getText());
		log.info("RegisterViewImpl onSubmit verifying fields eMail " + eMail);
		log.info("RegisterViewImpl onSubmit verifying fields password " + password);
		log.info("RegisterViewImpl onSubmit verifying fields username " + username);


		// can extend validation code here

		if (username.equals("") || password.equals("") || cpsswd.equals("") || email.equals("")) {
			Window.alert(RDL.i18n.enterRequiredData());
			return;
		}

		if (!FieldVerifier.isValidName(username)) {
			Window.alert(RDL.i18n.enterValidUserName());
			return;
		}

		if (!FieldVerifier.isValidName(eMail)) {
			Window.alert(RDL.i18n.enterValidEmail());
			return;
		}

		if (!FieldVerifier.isValidName(password)) {
			Window.alert(RDL.i18n.enterValidPass());
			return;
		}


		if (FieldVerifier.confirmPassword(psswd.getText(), cpsswd.getText())) {

			AutoBean<AuthUserBean> newUserBean = beanery.authBean();
			newUserBean.as().setAuth(false);
			newUserBean.as().setName(username);
			newUserBean.as().setEmail(eMail);
			newUserBean.as().setPassword(password);
			newUserBean.as().setIsRDLSupporter(false);
			newUserBean.as().setRep(0);
			presenter.submitNewUser(newUserBean);

		} else Window.alert(RDL.i18n.passwordsDoNotMatch());

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
