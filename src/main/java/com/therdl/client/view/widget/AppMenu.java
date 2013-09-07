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
import com.therdl.shared.events.LogOutEvent;


import java.util.logging.Logger;

public class AppMenu extends Composite  {

    private static Logger log = Logger.getLogger("");

    private static AppMenuUiBinder uiBinder = GWT.create(AppMenuUiBinder.class);
    @UiField MenuBar menuBar;

    @UiField MenuItem ideas;
    // ideas drop down
    @UiField MenuItem snipSearch;
    @UiField MenuItem snipEdit;

    @UiField MenuItem stories;
    @UiField MenuItem improvements;
    @UiField MenuItem services;


    // auth flow
    @UiField MenuItem userdetails;
    @UiField MenuItem user;
    @UiField MenuItem email;
    @UiField MenuItem out;
    @UiField MenuItem signUp;

    interface AppMenuUiBinder extends UiBinder<Widget, AppMenu> {
    }

    public AppMenu() {
        initWidget(uiBinder.createAndBindUi(this));

        // temp initialisation settings for unused functions
        this .stories.setVisible(false);
        this .services.setVisible(false);
        this .improvements.setVisible(false);
        // set the style name to position the user dropdown far right
        this .userdetails.setStyleName("userDropDown");
        this .signUp.setStyleName("signUpAccount");
        this .ideas.setStyleName("ideasMenuItem");
        this .stories.setStyleName("stories");;
        this .services.setStyleName("services");
        this .improvements.setStyleName("improvements");

        snipEdit.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens.edit");
                History.newItem(RDLConstants.Tokens.SNIP_EDIT);
            }
        });

        snipSearch.setScheduledCommand(new Scheduler.ScheduledCommand() {
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
                log.info("AppMenu: History.newItem RDLConstants.TokenslogoutS");
                GuiEventBus.EVENT_BUS.fireEvent(new LogOutEvent());
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

    public void setMainGroupVisible(boolean state) {
        log.info("AppMenu: setMainGroupVisible "+state);
        this .stories.setVisible(state);
        this .services.setVisible(state);
        this .improvements.setVisible(state);


    }

}
