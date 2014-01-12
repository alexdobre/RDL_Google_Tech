package com.therdl.client.view.widget;

import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.NavHeader;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogInEvent;
import com.therdl.shared.events.LogOutEvent;

import java.util.logging.Logger;


/**
 *   Application menu often referred  to as a 'NavBar' in a TwitterBootstrap scheme for example
 *
 *   @MenuItem menuBar, ideas, home, improvements, profile, userdetails, user, email, out, signUp, login.
 *
 *   all these menu items can trigger events, when clicked in the widget the following  inner class
 *   ScheduledCommand  is created <menu item>.setScheduledCommand(<Scheduler.ScheduledCommand> class)
 *   this can then fire a event, in this application these events break down into two types
 *   1. current history event with a History token
 *   2. GuiEventBus.EVENT_BUS event, these events can be found in com.therdl.shared.events
 *
 *   @ void setUser(String id) sets the user name in the menu display
 *   @ void setEmail(String id) sets the user email in the menu display
 *   @ void setSignUpVisible (boolean state) sets the SignUp in the menu display
 *   @ void setUserInfoVisible (boolean state) sets the UserInfo in the menu display
 *   @ void setLogOutVisible(boolean state) sets the LogOut in the menu display
 *   @ void setLogInVisible(boolean state) sets the LogIn in the menu display
 *   @ void setMainGroupVisible(boolean state) sets the MainGroup (login, signup) in the menu display
 *   @ void setSignUpView() sets the SignUpView widget into focus for user login
 */
public class AppMenu extends Composite  {

    private static Logger log = Logger.getLogger("");

    private static AppMenuUiBinder uiBinder = GWT.create(AppMenuUiBinder.class);

    @UiField Brand home;
    @UiField NavLink ideas;
    @UiField NavLink stories;
    @UiField NavLink improvements;
    @UiField NavLink signUp;
    @UiField NavLink login;


    // auth flow
    @UiField Dropdown userdetails;
    @UiField NavHeader user;
    @UiField NavLink email;
    @UiField NavLink profile;
    @UiField NavLink out;


    interface AppMenuUiBinder extends UiBinder<Widget, AppMenu> {
    }

    public AppMenu() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("profile")
    public void onProfileClick(ClickEvent event) {
        log.info("AppMenu: History.newItem RDLConstants.Tokens PROFILE");
        History.newItem(RDLConstants.Tokens.PROFILE + ":" + user.getText());
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

    @UiHandler("signUp")
    public void onSignUpClick(ClickEvent event) {
        log.info("AppMenu: History.newItem RDLConstants.Tokens.Signup");
        History.newItem(RDLConstants.Tokens.SIGN_UP);
    }

    /**
     * Sets the signUp element as active in the menu
     */
    public void setSignUpActive(){
        signUp.setActive(true);
    }

    @UiHandler("home")
    public void onHomeClick(ClickEvent event) {
        log.info("AppMenu: History.newItem RDLConstants.Tokens home");
        History.newItem(RDLConstants.Tokens.WELCOME);
    }

    /**
     * Sets the home element as active in the menu
     */
    public void setHomeActive(){
        home.setEnabled(false);
        home.addStyleName("brandActive");
    }

    @UiHandler("ideas")
    public void onIdeasClick(ClickEvent event) {
        log.info("AppMenu: History.newItem RDLConstants.Tokens.ideas");
        History.newItem(RDLConstants.Tokens.SNIPS);
    }

    /**
     * Sets the ideas element as active in the menu
     */
    public void setIdeasActive(){
        ideas.setActive(true);
    }

    @UiHandler("stories")
    public void onStoriesClick(ClickEvent event) {
        log.info("AppMenu: History.newItem RDLConstants.Tokens.stories");
        History.newItem(RDLConstants.Tokens.STORIES);
    }

    /**
     * Sets the stories element as active in the menu
     */
    public void setStoriesActive(){
        stories.setActive(true);
    }

    @UiHandler("improvements")
    public void onImprovementsClick(ClickEvent event) {
        log.info("AppMenu: History.newItem RDLConstants.Tokens.improvements");
        History.newItem(RDLConstants.Tokens.IMPROVEMENTS);
    }

    /**
     * Sets the improvements element as active in the menu
     */
    public void setImprovementsActive(){
        improvements.setActive(true);
    }

    /**
     * displays the username
     *
     */
    public void setUser(String id) {
        log.info("AppMenu:setUser "+id);
        user.setText(id);
    }

    /**
     * displays the email string
     *
     */
    public void setEmail(String id) {
        log.info("AppMenu:setEmail "+id);
        email.setText(id);

    }

    /**
     * displays the SignUp option
     *
     */
    public void  setSignUpVisible (boolean state){
        log.info("AppMenu: setSignUpVisible "+state);
        signUp.setVisible(state);
    }

    /**
     * displays the UserInfo details in a drop down
     *
     */
    public void  setUserInfoVisible (boolean state) {
        log.info("AppMenu: setUserInfoVisible "+state);
        userdetails.setVisible(state);
        user.setVisible(state);
        email.setVisible(state);
    }

    /**
     * displays the LogOut option
     *
     */
    public void setLogOutVisible(boolean state) {
        log.info("AppMenu: setLogOutVisible "+state);
        this.out.setVisible(state);

    }

    /**
     * displays the LogIn option
     *
     */
    public void setLogInVisible(boolean state) {
        log.info("AppMenu: setLogInVisible "+state);
        this.login.setVisible(state);

    }

    /**
     * displays the MainGroup of items currently only Improvements
     * @param state
     */
    public void setMainGroupVisible(boolean state) {
        log.info("AppMenu: setMainGroupVisible "+state);
        this .improvements.setVisible(state);


    }


    /**
     * sets the sign up display options
     */
    public void setSignUpView() {
        log.info("AppMenu: setSignUpView ");
        this.login.setVisible(false);
        setUserInfoVisible (false);
    }

}
