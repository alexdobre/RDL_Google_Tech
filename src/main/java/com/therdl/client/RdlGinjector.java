package com.therdl.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.therdl.client.app.AppController;

/**
 * The google-gin injector for client side dependency injection
 * for more info please see https://code.google.com/p/google-gin/wiki/GinTutorial
 *
 * @author alex
 */
@GinModules(RdlClientModule.class)
public interface RdlGinjector extends Ginjector {

	public AppController getAppController();

	//public AppMenu getAppMenu();
}

