package com.therdl.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.therdl.client.app.AppController;
import com.therdl.client.view.widget.WidgetHolder;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RDL implements EntryPoint {


  private static Logger sLogger = Logger.getLogger("");
  private final Messages messages = GWT.create(Messages.class);

  public void onModuleLoad() {

      sLogger.info("Refactoring to a loosley coupled web application");
      sLogger.info("http://en.wikipedia.org/wiki/Loose_coupling");
      sLogger.info("Ajax logging window enabled");

      //OK so what will happen on our model load
      //Initialize logging
      GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
          public void onUncaughtException(Throwable e) {
              sLogger.log(Level.INFO, e.getMessage(), e);
          }
      });

      //Firstly we initialize the AppController and it's items like EventBus
      final EventBus eventBus = new SimpleEventBus();


      AppController appController = new AppController(eventBus);
      WidgetHolder.getInstance();
      //we let the appController take over
      appController.go(RootLayoutPanel.get());



  }
}
