package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when an operation with snips was invalid
 */
public class SnipValidationException extends RdlCodedException {

	public SnipValidationException(String code) {
		super(code);
	}
}
