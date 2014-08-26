package com.therdl.shared.exceptions;

/**
 * Thrown when an operation with snips was invalid
 */
public class SnipValidationException extends RdlCodedException {

	public SnipValidationException(String code) {
		super(code);
	}
}
