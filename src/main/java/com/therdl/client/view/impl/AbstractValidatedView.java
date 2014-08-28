package com.therdl.client.view.impl;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.FormErrors;
import com.therdl.client.view.widget.FormSuccess;

/**
 * Default implementation of ValidatedView
 */
public abstract class AbstractValidatedView extends Composite implements ValidatedView {

	@UiField
	public FormErrors formErrors;

	@UiField
	public FormSuccess formSuccess;

	@Override
	public void setErrorMessage(String errorMessage){
		formErrors.setErrorMessage(errorMessage);
		ViewUtils.showHide(false, formSuccess);
	}

	@Override
	public void setSuccessMessage (String successMessage){
		formSuccess.setSuccessMessage(successMessage);
		ViewUtils.showHide(false,formErrors);
	}

	@Override
	public void hideMessages (){
		ViewUtils.showHide(false,formErrors);
		ViewUtils.showHide(true,formSuccess);
	}
}
