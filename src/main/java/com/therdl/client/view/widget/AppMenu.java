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

public class AppMenu extends Composite  {

    private static Logger log = Logger.getLogger("");

    private static AppMenuUiBinder uiBinder = GWT.create(AppMenuUiBinder.class);
    @UiField MenuBar menuBar;

    @UiField MenuItem ideas;
    @UiField MenuItem home;
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
        this .improvements.setStyleName("improvements");
        this .login.setStyleName("login");


        ideas.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens.search");
                History.newItem(RDLConstants.Tokens.SNIPS);
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
                History.newItem(RDLConstants.Tokens.PROFILE);
            }
        });


    }

    public void setUser(String id) {
        log.info("AppMenu:setUser "+id);
        user.setHTML(id);


    }

    public void setEmail(String id) {
        log.info("AppMenu:setEmail "+id);
        email.setHTML(id);

    }


    public void  setSignUpVisible (boolean state){
        log.info("AppMenu: setSignUpVisible "+state);
        signUp.setVisible(state);
    }

    public void  setUserInfoVisible (boolean state){
        log.info("AppMenu: setUserInfoVisible "+state);
        userdetails.setVisible(state);
        user.setVisible(state);
        email.setVisible(state);
    }

    public void setLogOutVisible(boolean state) {
        log.info("AppMenu: setLogOutVisible "+state);
        this.out.setVisible(state);

    }

    public void setLogInVisible(boolean state) {
        log.info("AppMenu: setLogInVisible "+state);
        this.login.setVisible(state);

    }

    public void setMainGroupVisible(boolean state) {
        log.info("AppMenu: setMainGroupVisible "+state);
        this .improvements.setVisible(state);


    }



    public void setSignUpView() {
        log.info("AppMenu: setSignUpView ");
        this.login.setVisible(false);
        setMainGroupVisible(false);
        setSignUpVisible(false);
        setUserInfoVisible (false);

    }

}
