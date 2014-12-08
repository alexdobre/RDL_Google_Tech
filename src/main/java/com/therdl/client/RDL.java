package com.therdl.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.therdl.client.app.AppController;
import com.therdl.client.app.LogicHolder;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.view.cssbundles.Resources;
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

	private final RdlGinjector injector = GWT.create(RdlGinjector.class);

	public static I18NConstants getI18n() {
		return i18n;
	}

	public static final I18NConstants i18n = GWT.create(I18NConstants.class);

	public void onModuleLoad() {

		//Initialize logging
		Log.setUncaughtExceptionHandler();
		AppController appController = injector.getAppController();

		RootLayoutPanel rp = RootLayoutPanel.get();
		//panels added to make the root panel stretch dynamically based on content
		SimplePanel p1 = new SimplePanel();
		p1.getElement().getStyle().setProperty("overflow", "auto");
		p1.getElement().getStyle().setProperty("position", "absolute");
		p1.getElement().getStyle().setProperty("zoom", "1");
		p1.getElement().getStyle().setProperty("left", "0px");
		p1.getElement().getStyle().setProperty("top", "0px");
		p1.getElement().getStyle().setProperty("right", "0px");
		p1.getElement().getStyle().setProperty("bottom", "0px");
		rp.add(p1);
		SimplePanel p2 = new SimplePanel();
		p2.getElement().getStyle().setProperty("position", "relative");
		p2.getElement().getStyle().setProperty("zoom", "1");
		p1.add(p2);

		// Inject the contents of the CSS file
		Resources.INSTANCE.rdlCss().ensureInjected();
		StyleInjector.inject(Resources.INSTANCE.rdlMediaCss().getText());
		//we let the appController take over
		appController.setInjector(injector);
		WidgetHolder.initHolder(appController);
		LogicHolder.initHolder(appController);
		appController.go(p2);
	}

}
