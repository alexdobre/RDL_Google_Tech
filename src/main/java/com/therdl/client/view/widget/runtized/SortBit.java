package com.therdl.client.view.widget.runtized;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A couple of sort arrows
 */
public class SortBit extends Composite {
	interface SortBitUiBinder extends UiBinder<Widget, SortBit> {
	}

	private static SortBitUiBinder uiBinder = GWT.create(SortBitUiBinder.class);


	public SortBit() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
