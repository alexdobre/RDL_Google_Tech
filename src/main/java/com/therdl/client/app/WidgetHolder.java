package com.therdl.client.app;

import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.widget.runtized.ReplyEditWidget;

/**
 * Because the AppController is getting a bit bloated we place the long lived widget implementations inside this
 * WidgetHolder class used as a factory class for widgets.
 * Singleton implementation and initialized at the same time as the AppController
 */
public class WidgetHolder {

	private static WidgetHolder holder;
	private AppController controller;

	private ReplyEditWidget replyEditWidget;

	private WidgetHolder (AppController controller) {
		this.controller = controller;
	}

	/**
	 * Initializes the widget holder
	 */
	public static void initHolder (AppController controller) {
		holder = new WidgetHolder(controller);
	}

	public static WidgetHolder getHolder() {
		return holder;
	}


	public ReplyEditWidget getReplyEditWidget(ReplyRunt replyRunt) {
		if (replyEditWidget == null) {
			replyEditWidget = new ReplyEditWidget(replyRunt);
		} else {
			replyEditWidget.setReplyRunt(replyRunt);
		}
		return replyEditWidget;
	}


}
