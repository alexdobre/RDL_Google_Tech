package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the Exterior core category of RDL
 * Created by Alex on 1/11/14.
 */
public class ExteriorDescription extends Composite {

	interface ExteriorDescriptionUiBinder extends UiBinder<HTMLPanel, ExteriorDescription> {
	}

	private static ExteriorDescriptionUiBinder uiBinder = GWT.create(ExteriorDescriptionUiBinder.class);

	public ExteriorDescription() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}