package com.therdl.shared.exceptions;

import com.therdl.shared.RDLConstants;

public class RDLSendEmailException extends RdlCodedException {

	public RDLSendEmailException() {
		super(RDLConstants.ErrorCodes.C004);
	}

	public RDLSendEmailException(String code) {
		super(code);
	}
}
