package com.therdl.shared.exceptions;

import java.io.Serializable;

public class RDLSendEmailException extends Exception implements Serializable {

	public RDLSendEmailException() {
	}

	public RDLSendEmailException(String message) {
		super(message);
	}
}
