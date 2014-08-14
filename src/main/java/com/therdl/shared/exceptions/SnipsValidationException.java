package com.therdl.shared.exceptions;

import java.io.Serializable;

import com.therdl.client.RDL;

/**
 * Thrown when an operation with snips was invalid
 */
public class SnipsValidationException extends Exception implements Serializable {

	public SnipsValidationException() {
		super (RDL.getI18n().errorSnipsGeneric());
	}

	public SnipsValidationException(String message) {
		super(message);
	}
}
