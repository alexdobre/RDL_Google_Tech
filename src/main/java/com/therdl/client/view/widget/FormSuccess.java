package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.common.ViewUtils;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * Displays success paragraphs
 */
public class FormSuccess extends Composite {

	interface FormSuccessUiBinder extends UiBinder<Widget, FormSuccess> {
	}

	@UiField
	Paragraph successMessage;

	private static FormSuccessUiBinder ourUiBinder = GWT.create(FormSuccessUiBinder.class);

	public FormSuccess() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public void setSuccessMessage(String msg) {
		successMessage.setText(msg);
		ViewUtils.showHide(true, this);
	}
}
