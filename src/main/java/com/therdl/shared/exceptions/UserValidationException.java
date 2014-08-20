package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when a validation went wrong involving a user
 */
public class UserValidationException extends RdlCodedException  {


	public UserValidationException(String code) {
		super(code);
	}
}