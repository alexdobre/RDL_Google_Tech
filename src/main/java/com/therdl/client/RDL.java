package com.therdl.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.therdl.client.app.AppController;
import com.therdl.client.view.i18n.I18NConstants;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @ Logger sLogger see http://www.gwtproject.org/doc/latest/DevGuideLogging.html
 * see  resources/com/therdl/rdl.gwt.xml for configuratiom
 * for internationalisation
 * @ AppController appController see com.therdl.client.app.AppController java doc
 */
public class RDL implements EntryPoint {


	private static Logger sLogger = Logger.getLogger("");

	public static I18NConstants getI18n() {
		return i18n;
	}

	public static final I18NConstants i18n = GWT.create(I18NConstants.class);

	public void onModuleLoad() {
		//Initialize logging
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				sLogger.log(Level.INFO, e.getMessage(), e);
			}
		});
		AppController appController = new AppController();
		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.setSize("100%", "3500px");
		//we let the appController take over
		appController.go(rp);
	}

}
