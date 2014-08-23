package com.therdl.shared.exceptions;

import com.therdl.shared.RDLConstants;

/**
 * User provided an invalid input
 */
public class InvalidInputException extends RdlCodedException  {

	public InvalidInputException(String code) {
		super(code);
	}

	public InvalidInputException (){
		super (RDLConstants.ErrorCodes.C003);
	}
}
