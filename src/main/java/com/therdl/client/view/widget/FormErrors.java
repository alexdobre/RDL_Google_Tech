package com.therdl.client.view.widget;

import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.common.ViewUtils;

/**
 * Displays error paragraphs
 */
public class FormErrors extends Composite {

	interface FormErrorsUiBinder extends UiBinder<Widget, FormErrors> {
	}

	@UiField
	Paragraph errorMessage;

	private static FormErrorsUiBinder ourUiBinder = GWT.create(FormErrorsUiBinder.class);

	public FormErrors() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public void setErrorMessage(String msg) {
		errorMessage.setText(msg);
		ViewUtils.showHide(true, this);
	}
}
