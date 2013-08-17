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
	@UiField MenuItem fastCaps;
	@UiField MenuItem snips;
	@UiField MenuItem materials;
	@UiField MenuItem groups;
	@UiField MenuItem services;
	@UiField MenuItem profile;
	@UiField MenuItem overview;
	@UiField MenuItem ovwRDL;
	@UiField MenuItem ovwCompatibility;
	@UiField MenuItem ovwEmmSupport;
	@UiField MenuItem ovwSensibilities;
	@UiField MenuItem ovwAtraction;
	@UiField MenuItem ovwBasic;
	@UiField MenuItem ovwAdvanced;
	@UiField MenuItem ovwSeduction;
	@UiField MenuItem ovwPsy;
	@UiField MenuItem ovwAffairs;
	@UiField MenuItem ovwModules;
	@UiField MenuItem ovwModulesSnips;
	@UiField MenuItem ovwModulesFastCaps;
	@UiField MenuItem ovwModulesMaterials;
    @UiField MenuItem ovwModulesGroups;
    @UiField MenuItem ovwModulesServices;
    // auth flow
    @UiField MenuItem user;
    @UiField MenuItem email;
    @UiField MenuItem out;

	interface AppMenuUiBinder extends UiBinder<Widget, AppMenu> {
	}

	public AppMenu() {
		initWidget(uiBinder.createAndBindUi(this));
		
		fastCaps.setScheduledCommand(new Scheduler.ScheduledCommand() {			
			@Override
			public void execute() {
                log.info("AppMenu: fastCaps.setScheduledCommand");
			}
		});
		snips.setScheduledCommand(new Scheduler.ScheduledCommand() {			
			@Override
			public void execute() {
                log.info("AppMenu: History.newItem RDLConstants.Tokens.SNIPS");
				History.newItem(RDLConstants.Tokens.SNIPS);			
			}
		});


        out.setScheduledCommand (new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                GuiEventBus.EVENT_BUS.fireEvent(new LogOutEvent());
            }
        });


        ovwModulesFastCaps.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: sidebar fastCaps setScheduledCommand");
            }
        });

        ovwModulesSnips.setScheduledCommand(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                log.info("AppMenu: sidebar History.newItem RDLConstants.Tokens.SNIPS");
                History.newItem(RDLConstants.Tokens.SNIPS);
            }
        });
	}

    public void setUser(String id) {

        user.setText(id);

    }

    public void setEmail(String id) {


       email.setText(id);
    }

}
