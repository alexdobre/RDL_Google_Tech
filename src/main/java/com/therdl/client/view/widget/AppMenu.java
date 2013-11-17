package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
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
    @UiField MenuBar menuBar;

    @UiField MenuItem home;
    @UiField MenuItem ideas;
    @UiField MenuItem stories;
    @UiField MenuItem improvements;
    @UiField MenuItem profile;

    // auth flow
    @UiField MenuItem userdetails;
    @UiField MenuItem user;
    @UiField MenuItem email;
    @UiField MenuItem out;
    @UiField MenuItem signUp;
    @UiField MenuItem login;

    interface AppMenuUiBinder extends UiBinder<Widget, AppMenu> {
    }

    public AppMenu() {
        initWidget(uiBinder.createAndBindUi(this));

        // set the style name to position the user dropdown far right
        this .userdetails.setStyleName("userDropDown");
        this .signUp.setStyleName("signUpAccount");
        this .home.setStyleName("homeMenuItem");
        this .ideas.setStyleName("ideasMenuItem");
        this .stories.setStyleName("storiesMenuItem");
        this .improvements.setStyleName("improvementsMenuItem");
        this .login.setStyleName("login");


        ideas.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens.search");
                History.newItem(RDLConstants.Tokens.SNIPS);
            }
        });

        stories.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens.stories");
                History.newItem(RDLConstants.Tokens.STORIES);
            }
        });

        improvements.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens.improvements");
                History.newItem(RDLConstants.Tokens.IMPROVEMENTS);
            }
        });

        signUp.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens.Signup");
                History.newItem(RDLConstants.Tokens.SIGN_UP);
            }
        });



        out.setScheduledCommand (new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: logout");
                GuiEventBus.EVENT_BUS.fireEvent(new LogOutEvent());
            }
        });

        login.setScheduledCommand (new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: login");
                GuiEventBus.EVENT_BUS.fireEvent(new LogInEvent());
            }
        });

        home.setScheduledCommand (new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens home");
                History.newItem(RDLConstants.Tokens.WELCOME);
            }
        });

        profile.setScheduledCommand (new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens PROFILE");
                History.newItem(RDLConstants.Tokens.PROFILE+":"+user.getText());
            }
        });


    }

    /**
     * displays the username
     * @param String id
     */
    public void setUser(String id) {
        log.info("AppMenu:setUser "+id);
        user.setHTML(id);
    }

    /**
     * displays the email string
     * @param String id
     */
    public void setEmail(String id) {
        log.info("AppMenu:setEmail "+id);
        email.setHTML(id);

    }

    /**
     * displays the SignUp option
     * @param boolean state
     */
    public void  setSignUpVisible (boolean state){
        log.info("AppMenu: setSignUpVisible "+state);
        signUp.setVisible(state);
    }

    /**
     * displays the UserInfo details in a drop down
     * @param boolean state
     */
    public void  setUserInfoVisible (boolean state) {
        log.info("AppMenu: setUserInfoVisible "+state);
        userdetails.setVisible(state);
        user.setVisible(state);
        email.setVisible(state);
    }

    /**
     * displays the LogOut option
     * @param boolean state
     */
    public void setLogOutVisible(boolean state) {
        log.info("AppMenu: setLogOutVisible "+state);
        this.out.setVisible(state);

    }

    /**
     * displays the LogIn option
     * @param boolean state
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
        setMainGroupVisible(false);
        setSignUpVisible(false);
        setUserInfoVisible (false);

    }

}
