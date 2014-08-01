package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the Connection core category of RDL
 * Created by Alex on 1/11/14.
 */
public class ConnectionDescription extends Composite {

	interface ConnectionDescriptionUiBinder extends UiBinder<HTMLPanel, ConnectionDescription> {
	}

	private static ConnectionDescriptionUiBinder uiBinder = GWT.create(ConnectionDescriptionUiBinder.class);

	public ConnectionDescription() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}