package com.therdl.shared.exceptions;

import com.therdl.client.RDL;
import java.io.Serializable;

/**
 * Thrown when an avatar file input was invalid
 */

public class AvatarInvalidException extends Exception implements Serializable {

	public AvatarInvalidException() {
		super (RDL.getI18n().errorAvatarUpload());
	}

	public AvatarInvalidException(String message) {
		super(message);
	}
}