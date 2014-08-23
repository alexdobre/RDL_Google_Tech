package com.therdl.client.view;

/**
 * A validated view contains error and success messages
 */
public interface ValidatedView {

	public void setErrorMessage(String errorMessage);

	public void setSuccessMessage (String successMessage);

	public void hideMessages();
}
