package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when an operation with snips was invalid
 */
public class SnipsValidationException extends Exception implements Serializable {

	public SnipsValidationException() {
		super ("SnipsValidationException");
	}

	public SnipsValidationException(String message) {
		super(message);
	}
}
