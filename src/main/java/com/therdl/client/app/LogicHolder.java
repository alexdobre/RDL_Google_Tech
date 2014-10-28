package com.therdl.client.app;

import com.therdl.client.logic.LinkedSortBit;

/**
 * Similar to the widget holder this contains logic classes that need to persist
 */
public class LogicHolder {

	private static LogicHolder holder;
	private AppController controller;

	private LinkedSortBit serviceFilterLinkedSortBit;

	private LogicHolder(AppController controller) {
		this.controller = controller;
	}

	/**
	 * Initializes the widget holder
	 */
	public static void initHolder(AppController controller) {
		holder = new LogicHolder(controller);
	}

	public static LogicHolder getHolder() {
		return holder;
	}

	public AppController getController() {
		return controller;
	}

	public LinkedSortBit getServiceFilterLinkedSortBit() {
		return serviceFilterLinkedSortBit;
	}

	public void setServiceFilterLinkedSortBit(LinkedSortBit serviceFilterLinkedSortBit) {
		this.serviceFilterLinkedSortBit = serviceFilterLinkedSortBit;
	}
}
