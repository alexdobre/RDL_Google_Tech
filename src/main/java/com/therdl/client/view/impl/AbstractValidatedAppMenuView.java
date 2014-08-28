package com.therdl.client.view.impl;

import com.google.gwt.uibinder.client.UiField;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.FormErrors;
import com.therdl.client.view.widget.FormSuccess;

/**
 * A validated view with an AppMenu
 */
public abstract class AbstractValidatedAppMenuView extends AppMenuView implements ValidatedView{

	@UiField
	protected FormErrors formErrors;

	@UiField
	protected FormSuccess formSuccess;

	public AbstractValidatedAppMenuView (AppMenu appMenu){
		super(appMenu);
	}


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
