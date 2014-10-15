package com.therdl.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.therdl.client.app.AppController;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.view.i18n.I18NConstants;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @ Logger sLogger see http://www.gwtproject.org/doc/latest/DevGuideLogging.html
 * see  resources/com/therdl/rdl.gwt.xml for configuratiom
 * for internationalisation
 * @ AppController appController see com.therdl.client.app.AppController java doc
 */
public class RDL implements EntryPoint {

	private static Logger sLogger = Logger.getLogger(RDL.class.getName());

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
		//panels added to make the root panel stretch dynamically based on content
		SimplePanel p1 = new SimplePanel();
		p1.getElement().getStyle().setProperty("overflow","auto");
		p1.getElement().getStyle().setProperty("position","absolute");
		p1.getElement().getStyle().setProperty("zoom","1");
		p1.getElement().getStyle().setProperty("left","0px");
		p1.getElement().getStyle().setProperty("top","0px");
		p1.getElement().getStyle().setProperty("right","0px");
		p1.getElement().getStyle().setProperty("bottom","0px");
		rp.add(p1);
		SimplePanel p2 = new SimplePanel();
		p2.getElement().getStyle().setProperty("position","relative");
		p2.getElement().getStyle().setProperty("zoom","1");
		p1.add(p2);

		WidgetHolder.initHolder(appController);
		//we let the appController take over
		appController.go(p2);
	}

}
