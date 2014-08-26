package com.therdl.shared.exceptions;

/**
 * Thrown when a validation went wrong involving a user
 */
public class UserValidationException extends RdlCodedException {


	public UserValidationException(String code) {
		super(code);
	}
}