package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when a validation went wrong involving a user
 */
public class UserValidationException extends Exception implements Serializable {

	public UserValidationException() {
		super ("UserValidationException");
	}

	public UserValidationException(String message) {
		super(message);
	}

}