package com.therdl.shared.exceptions;

import com.therdl.shared.RDLConstants;

import java.io.Serializable;

/**
 * Thrown when a tokenized operation did not supply the correct token
 */
public class TokenInvalidException extends RdlCodedException {

	public TokenInvalidException() {
		super(RDLConstants.ErrorCodes.GENERIC);
	}

	public TokenInvalidException(String message) {
		super(message);
	}
}
