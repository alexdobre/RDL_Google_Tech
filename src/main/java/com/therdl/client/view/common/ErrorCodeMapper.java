package com.therdl.client.view.common;

import com.therdl.client.RDL;
import com.therdl.shared.RDLConstants;

/**
 * Maps server side error codes to i18n interface messages
 */
public class ErrorCodeMapper {

	public static String getI18nMessage (String errorCode){

		switch (errorCode){
		case RDLConstants.ErrorCodes.C001: return RDL.getI18n().serverErrorC001();
		case RDLConstants.ErrorCodes.C002: return RDL.getI18n().serverErrorC002();
		default: return RDL.getI18n().serverErrorC000();
		}
	}
}
