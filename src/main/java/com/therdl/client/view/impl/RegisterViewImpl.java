package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.RegisterView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.FormErrors;
import com.therdl.client.view.widget.FormSuccess;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;

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
public class RegisterViewImpl extends AbstractValidatedAppMenuView implements RegisterView {

	private static Logger log = Logger.getLogger(RegisterViewImpl.class.getName());

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
	Input psswd;

	@UiField
	Input cpsswd;

	@UiField
	Button submitBtn;

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
		AutoBean<AuthUserBean> newUserBean = formAuthUserBean();
		presenter.submitNewUser(newUserBean);
	}

	private AutoBean<AuthUserBean> formAuthUserBean() {
		AutoBean<AuthUserBean> newUserBean = beanery.authBean();
		newUserBean.as().setAuth(false);
		newUserBean.as().setName(userName.getText());
		newUserBean.as().setEmail(email.getText());
		newUserBean.as().setPassword(psswd.getText());
		newUserBean.as().setPasswordConfirm(cpsswd.getText());
		newUserBean.as().setIsRDLSupporter(false);
		newUserBean.as().setRep(0);
		return newUserBean;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
