package com.therdl.shared.exceptions;

import java.io.Serializable;

import com.therdl.client.RDL;

/**
 * Thrown when a tokenized operation did not supply the correct token
 */
public class TokenInvalidException extends Exception implements Serializable {

	public TokenInvalidException() {
		super(RDL.getI18n().errorTokenGeneric());
	}

	public TokenInvalidException(String message) {
		super(message);
	}
}
