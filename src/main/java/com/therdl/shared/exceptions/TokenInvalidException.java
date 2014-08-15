package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when a tokenized operation did not supply the correct token
 */
public class TokenInvalidException extends Exception implements Serializable {

	public TokenInvalidException() {
		super("TokenInvalidException");
	}

	public TokenInvalidException(String message) {
		super(message);
	}
}
