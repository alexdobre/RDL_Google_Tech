package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * Thrown when an unexpected result was received from the server
 */
public class ServerSideException extends Exception implements Serializable {

	public ServerSideException() {
		super ("ServerSideException");
	}

	public ServerSideException(String message) {
		super(message);
	}

}
