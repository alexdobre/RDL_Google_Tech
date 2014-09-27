package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.handler.LoginHandler;
import com.therdl.client.presenter.CommonPresenter;
import com.therdl.client.presenter.ForgotPasswordPresenter;
import com.therdl.client.view.ForgotPassword;
import com.therdl.client.view.impl.ForgotPasswordImpl;
import com.therdl.client.view.impl.SignInViewImpl;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.BecomeRdlSupporterEvent;
import com.therdl.shared.events.BecomeRdlSupporterEventHandler;
import com.therdl.shared.events.CredentialsSubmitEvent;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogInEvent;
import com.therdl.shared.events.LogInEventEventHandler;
import com.therdl.shared.events.LogInOkEvent;
import com.therdl.shared.events.LogInOkEventEventHandler;
import com.therdl.shared.events.LogOutEvent;
import com.therdl.shared.events.ReportAbuseEvent;
import com.therdl.shared.events.ReportAbuseEventHandler;
import com.therdl.shared.events.ShowAbuseCommentsEvent;
import com.therdl.shared.events.ShowAbuseCommentsEventHandler;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavbarBrand;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.TextBox;

import java.util.logging.Logger;

/**
 * Application menu often referred  to as a 'NavBar' in a TwitterBootstrap scheme for example
 */
public class AppMenu extends Composite {

	private static Logger log = Logger.getLogger("");

	private static AppMenuUiBinder uiBinder = GWT.create(AppMenuUiBinder.class);

	private SignInViewImpl signInView;
	private ForgotPassword forgotPassword;
	private SupportRdlPopup supportRdlPopup;
	private ReportAbusePopup reportAbusePopup;
	private AbuseCommentsPopup abuseCommentsPopup;
	private CommonPresenter presenter;
	LoginHandler loginHandler;

	@UiField
	NavbarBrand home;
	@UiField
	AnchorListItem ideas;
	@UiField
	AnchorListItem stories;
	@UiField
	AnchorListItem tribunal;
	@UiField
	AnchorListItem improvements;
	@UiField
	AnchorListItem signUp;
	@UiField
	AnchorListItem login;
	@UiField
	AnchorButton userDetails;
	@UiField
	NavbarHeader user;
	@UiField
	AnchorListItem email;
	@UiField
	AnchorListItem profile;
	@UiField
	AnchorListItem out;
	@UiField
	TextBox searchSiteContent;

	interface AppMenuUiBinder extends UiBinder<Widget, AppMenu> {
	}

	public AppMenu(final CommonPresenter presenter) {
		initWidget(uiBinder.createAndBindUi(this));
		logOut();
		this.presenter = presenter;
		GuiEventBus.EVENT_BUS.addHandler(LogInEvent.TYPE, new LogInEventEventHandler() {
			@Override
			public void onLogInEvent(LogInEvent onLoginEvent) {
				showLoginPopUp(null);
			}
		});
		// user has just successfully logged in update app menu
		GuiEventBus.EVENT_BUS.addHandler(LogInOkEvent.TYPE, new LogInOkEventEventHandler() {
			@Override
			public void onLogInOkEvent(LogInOkEvent onLoginOkEvent) {
				setAppMenu(onLoginOkEvent.getCurrentUserBean());
			}
		});
		GuiEventBus.EVENT_BUS.addHandler(BecomeRdlSupporterEvent.TYPE, new BecomeRdlSupporterEventHandler() {
			@Override
			public void onSupporterEvent(BecomeRdlSupporterEvent event) {
				if (supportRdlPopup == null) {
					supportRdlPopup = new SupportRdlPopup();
				}
				supportRdlPopup.setTitle(event.getPopupTitle());
				presenter.grabRdlSupporterTitleDesc(supportRdlPopup);
				supportRdlPopup.show();
			}
		});
		GuiEventBus.EVENT_BUS.addHandler(ReportAbuseEvent.TYPE, new ReportAbuseEventHandler() {
			@Override
			public void onAbuseEvent(ReportAbuseEvent event) {
				if (reportAbusePopup == null) {
					reportAbusePopup = new ReportAbusePopup(presenter);
				}
				reportAbusePopup.show(event.getContentId());
			}
		});
		GuiEventBus.EVENT_BUS.addHandler(ShowAbuseCommentsEvent.TYPE, new ShowAbuseCommentsEventHandler() {
			@Override
			public void onCommentsEvent(ShowAbuseCommentsEvent event) {
				if (abuseCommentsPopup == null) {
					abuseCommentsPopup = new AbuseCommentsPopup();
				}
				presenter.showAbuseComments(event.getAbusiveContent(), abuseCommentsPopup);
			}
		});
	}

	/**
	 * Sets the upper header Menu to the correct state for a given users auth state(eg logged in)
	 *
	 * @param currentUserBean
	 */

	public void setAppMenu(AutoBean<CurrentUserBean> currentUserBean) {
		if (currentUserBean.as().isAuth()) {
			log.info("ProfileViewImpl setAppMenu auth true " + currentUserBean.as().getName());
			logIn(currentUserBean);
		} else {
			logOut();
		}

	}

	public void logIn(AutoBean<CurrentUserBean> currentUserBean) {
		log.info("AppMenu logIn: " + currentUserBean);
		setLogOutVisible(true);
		setSignUpVisible(false);
		setUserInfoVisible(true);
		setUser(currentUserBean.as().getName());
		setEmail(currentUserBean.as().getEmail());
		setLogInVisible(false);
	}

	@UiHandler("profile")
	public void onProfileClick(ClickEvent event) {
		log.info("AppMenu: History.newItem RDLConstants.Tokens PROFILE");
		History.newItem(RDLConstants.Tokens.PROFILE + ":" + user.getTitle());
	}

	@UiHandler("out")
	public void onLogoutClick(ClickEvent event) {
		log.info("AppMenu: logout");
		GuiEventBus.EVENT_BUS.fireEvent(new LogOutEvent());
	}

	@UiHandler("login")
	public void onLoginClick(ClickEvent event) {
		log.info("AppMenu: login");
		GuiEventBus.EVENT_BUS.fireEvent(new LogInEvent());
	}

	@UiHandler("searchSiteContent")
	public void onSearchEnter(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			History.newItem(RDLConstants.Tokens.CONTENT_SEARCH + ":" + searchSiteContent.getText());
		}
	}

	private void allInactive() {
		home.removeStyleName("brandActive");
		ideas.setActive(false);
		stories.setActive(false);
		tribunal.setActive(false);
		improvements.setActive(false);
		signUp.setActive(false);
		login.setActive(false);
	}

	/**
	 * Sets the signUp element as active in the menu
	 */
	public void setSignUpActive() {
		allInactive();
		signUp.setActive(true);
	}

	/**
	 * Sets the home element as active in the menu
	 */
	public void setHomeActive() {
		allInactive();
		home.addStyleName("brandActive");
	}

	/**
	 * Sets the ideas element as active in the menu
	 */
	public void setIdeasActive() {
		allInactive();
		ideas.setActive(true);
	}

	/**
	 * Sets the stories element as active in the menu
	 */
	public void setStoriesActive() {
		allInactive();
		stories.setActive(true);
	}

	public void setActive() {
		if (Global.moduleName == RDLConstants.Modules.STORIES) setStoriesActive();
		if (Global.moduleName == RDLConstants.Modules.IDEAS) setIdeasActive();
		if (Global.moduleName == RDLConstants.Modules.IMPROVEMENTS) setImprovementsActive();
		if (Global.moduleName == RDLConstants.Modules.TRIBUNAL) setTribunalActive();
	}

	/**
	 * Sets the improvements element as active in the menu
	 */
	public void setImprovementsActive() {
		allInactive();
		improvements.setActive(true);
	}

	public void setTribunalActive() {
		allInactive();
		tribunal.setActive(true);
	}

	/**
	 * displays the username
	 */
	public void setUser(String id) {
		user.setTitle(id);
	}

	/**
	 * displays the email string
	 */
	public void setEmail(String id) {
		email.setText(id);

	}

	/**
	 * displays the SignUp option
	 */
	private void setSignUpVisible(boolean state) {
		signUp.setVisible(state);
	}

	/**
	 * displays the UserInfo details in a drop down
	 */
	private void setUserInfoVisible(boolean state) {
		userDetails.setVisible(state);
		user.setVisible(state);
		email.setVisible(state);
	}

	/**
	 * displays the LogOut option
	 */
	private void setLogOutVisible(boolean state) {
		this.out.setVisible(state);

	}

	/**
	 * displays the LogIn option
	 */
	private void setLogInVisible(boolean state) {
		this.login.setVisible(state);

	}

	/**
	 * sets the sign up display options
	 */
	public void setSignUpView() {
		this.login.setVisible(false);
		setUserInfoVisible(false);
	}

	public void logOut() {
		log.info("AppMenu logOut");
		this.setLogOutVisible(false);
		this.setSignUpVisible(true);
		this.setUserInfoVisible(false);
		this.setLogInVisible(true);
	}


	/**
	 * creates and shows login popup and sets its parameters
	 *
	 * @param loginHandler handler which is called when login is successful
	 */
	public void showLoginPopUp(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
		if (signInView == null) {
			signInView = new SignInViewImpl(this);
		}
		signInView.show();
	}

	public void showForgotPasswordPopUp() {
		if (forgotPassword == null) {
			forgotPassword = new ForgotPasswordImpl();
		}
		ForgotPasswordPresenter forgotPasswordPresenter = new ForgotPasswordPresenter(forgotPassword);
		forgotPasswordPresenter.showForgotPasswordPopup();
		signInView.hide();
	}

	/**
	 * called form signin view pop up to initiate log in flow
	 *
	 * @param emailTxt     supplied credential
	 * @param passwordText supplied credential
	 */
	public void onSubmit(String emailTxt, String passwordText, Boolean rememberMe) {
		GuiEventBus.EVENT_BUS.fireEvent(new CredentialsSubmitEvent(emailTxt, passwordText, rememberMe, null, loginHandler));
	}

	public TextBox getSearchSiteTextBox() {
		return searchSiteContent;
	}

	public ReportAbusePopup getReportAbusePopup() {
		return reportAbusePopup;
	}
}
