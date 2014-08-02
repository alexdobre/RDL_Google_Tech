package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogInEvent;
import com.therdl.shared.events.LogInOkEvent;
import com.therdl.shared.events.LogInOkEventEventHandler;
import com.therdl.shared.events.LogOutEvent;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavbarBrand;
import org.gwtbootstrap3.client.ui.NavbarHeader;

import java.util.logging.Logger;


/**
 * Application menu often referred  to as a 'NavBar' in a TwitterBootstrap scheme for example
 *
 * @MenuItem menuBar, ideas, home, improvements, profile, userdetails, user, email, out, signUp, login.
 * all these menu items can trigger events, when clicked in the widget the following  inner class
 * ScheduledCommand  is created <menu item>.setScheduledCommand(<Scheduler.ScheduledCommand> class)
 * this can then fire a event, in this application these events break down into two types
 * 1. current history event with a History token
 * 2. GuiEventBus.EVENT_BUS event, these events can be found in com.therdl.shared.events
 * @ void setUser(String id) sets the user name in the menu display
 * @ void setEmail(String id) sets the user email in the menu display
 * @ void setSignUpVisible (boolean state) sets the SignUp in the menu display
 * @ void setUserInfoVisible (boolean state) sets the UserInfo in the menu display
 * @ void setLogOutVisible(boolean state) sets the LogOut in the menu display
 * @ void setLogInVisible(boolean state) sets the LogIn in the menu display
 * @ void setMainGroupVisible(boolean state) sets the MainGroup (login, signup) in the menu display
 * @ void setSignUpView() sets the SignUpView widget into focus for user login
 */
public class AppMenu extends Composite {

	private static Logger log = Logger.getLogger("");

	private static AppMenuUiBinder uiBinder = GWT.create(AppMenuUiBinder.class);

	@UiField
	NavbarBrand home;
	@UiField
	AnchorListItem ideas;
	@UiField
	AnchorListItem stories;
	@UiField
	AnchorListItem improvements;
	@UiField
	AnchorListItem signUp;
	@UiField
	AnchorListItem login;


	// auth flow
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

	private static int i=0;

	interface AppMenuUiBinder extends UiBinder<Widget, AppMenu> {
	}

	public AppMenu() {
		initWidget(uiBinder.createAndBindUi(this));
		log.info("App menu constructor i++"+i++);

		// user has just sucessfully logged in update app menu
		GuiEventBus.EVENT_BUS.addHandler(LogInOkEvent.TYPE, new LogInOkEventEventHandler() {

			@Override
			public void onLogInOkEvent(LogInOkEvent onLoginOkEvent) {
				setAppMenu(onLoginOkEvent.getCurrentUserBean());
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

			setLogOutVisible(true);
			setSignUpVisible(false);
			setUserInfoVisible(true);
			setUser(currentUserBean.as().getName());
			setEmail(currentUserBean.as().getEmail());
			setLogInVisible(false);
		} else {

			setLogOutVisible(false);
			setSignUpVisible(true);
			setUserInfoVisible(false);
			setLogInVisible(true);
		}

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

	private void allInactive(){
		home.removeStyleName("brandActive");
		ideas.setActive(false);
		stories.setActive(false);
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

	/**
	 * Sets the improvements element as active in the menu
	 */
	public void setImprovementsActive() {
		allInactive();
		improvements.setActive(true);
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
	public void setSignUpVisible(boolean state) {
		signUp.setVisible(state);
	}

	/**
	 * displays the UserInfo details in a drop down
	 */
	public void setUserInfoVisible(boolean state) {
		userDetails.setVisible(state);
		user.setVisible(state);
		email.setVisible(state);
	}

	/**
	 * displays the LogOut option
	 */
	public void setLogOutVisible(boolean state) {
		this.out.setVisible(state);

	}

	/**
	 * displays the LogIn option
	 */
	public void setLogInVisible(boolean state) {
		this.login.setVisible(state);

	}

	/**
	 * displays the MainGroup of items currently only Improvements
	 *
	 * @param state
	 */
	public void setMainGroupVisible(boolean state) {
		this.improvements.setVisible(state);
	}


	/**
	 * sets the sign up display options
	 */
	public void setSignUpView() {
		this.login.setVisible(false);
		setUserInfoVisible(false);
	}

	public void setLoginResult(String name, String email, boolean auth) {
		if (auth) {
			log.info("SnipSearchViewImpl setloginresult auth true " + name);

			this.setLogOutVisible(true);
			this.setSignUpVisible(false);
			this.setUserInfoVisible(true);
			this.setUser(name);
			this.setEmail(email);
			this.setLogInVisible(false);
		} else {
			logOut();
		}
	}

	public void logOut() {
		this.setLogOutVisible(false);
		this.setSignUpVisible(true);
		this.setUserInfoVisible(false);
		this.setLogInVisible(true);

	}

}
