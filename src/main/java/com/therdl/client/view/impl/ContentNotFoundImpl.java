package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.RDL;
import com.therdl.client.view.ContentNotFound;
import com.therdl.client.view.widget.AppMenu;
import org.gwtbootstrap3.client.ui.Heading;

/**
 * Content not found page
 */
public class ContentNotFoundImpl extends AppMenuView implements ContentNotFound {

	interface ContentNotFoundUiBinder extends UiBinder<Widget, ContentNotFoundImpl> {
	}

	private static ContentNotFoundUiBinder ourUiBinder = GWT.create(ContentNotFoundUiBinder.class);

	private ContentNotFound.Presenter presenter;

	@UiField
	Heading message;

	public ContentNotFoundImpl (AppMenu appMenu){
		super(appMenu);
		initWidget(ourUiBinder.createAndBindUi(this));
		message.setText(RDL.getI18n().contentNotFound());
	}

	public void setMessage(String messStr){
		message.setText(RDL.getI18n().contentNotFound()+" : "+messStr);
	}
}
