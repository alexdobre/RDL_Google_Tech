package com.therdl.client.view;

/**
 * This is shown whenever the content the user was searching for is not found
 */
public interface ContentNotFound extends RdlView {

	public interface Presenter {
	}

	public void setMessage(String message);
}
