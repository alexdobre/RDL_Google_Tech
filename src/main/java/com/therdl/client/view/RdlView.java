package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.therdl.client.view.widget.AppMenu;

/**
 * Generic interface for every view
 */
public interface RdlView extends IsWidget {

	AppMenu getAppMenu();
}
