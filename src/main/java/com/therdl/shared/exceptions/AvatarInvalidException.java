package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when an avatar file input was invalid
 */

public class AvatarInvalidException extends Exception implements Serializable {

	public AvatarInvalidException() {
		super("AvatarInvalidException");
	}

	public AvatarInvalidException(String message) {
		super(message);
	}
}