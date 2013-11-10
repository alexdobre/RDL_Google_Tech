package com.therdl.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.therdl.client.app.AppController;
import com.therdl.shared.Messages;


import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @ Logger sLogger see http://www.gwtproject.org/doc/latest/DevGuideLogging.html
 * see  resources/com/therdl/rdl.gwt.xml for configuratiom
 * @ Messages messages see http://www.gwtproject.org/doc/latest/DevGuideI18nMessages.html
 * for internationalisation
 * @ AppController appController see com.therdl.client.app.AppController java doc
 *
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

      AppController appController = new AppController();
      // need this for landing page
      RootLayoutPanel rp = RootLayoutPanel.get();
      rp.setSize("100%","1500px");
      //we let the appController take over
      appController.go(rp);


  }
}
