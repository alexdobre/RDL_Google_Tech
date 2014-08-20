package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when an operation with snips was invalid
 */
public class SnipValidationException extends Exception implements Serializable {

	public SnipValidationException() {
		super ("SnipValidationException");
	}

	public SnipValidationException(String message) {
		super(message);
	}
}
