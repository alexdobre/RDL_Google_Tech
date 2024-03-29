package com.therdl.client.view.common;

import com.therdl.client.RDL;
import com.therdl.shared.RDLConstants;

/**
 * Maps server side error codes to i18n interface messages
 */
public class ErrorCodeMapper {

	public static String getI18nMessage (String errorCode){

		switch (errorCode){
		case RDLConstants.ErrorCodes.GENERIC: return RDL.getI18n().serverErrorC000();
		case RDLConstants.ErrorCodes.C001: return RDL.getI18n().serverErrorC001();
		case RDLConstants.ErrorCodes.C002: return RDL.getI18n().serverErrorC002();
		case RDLConstants.ErrorCodes.C003: return RDL.getI18n().serverErrorC003();
		case RDLConstants.ErrorCodes.C004: return RDL.getI18n().serverErrorC004();
		case RDLConstants.ErrorCodes.C005: return RDL.getI18n().serverErrorC005();
		case RDLConstants.ErrorCodes.C006: return RDL.getI18n().serverErrorC006();
		case RDLConstants.ErrorCodes.C007: return RDL.getI18n().serverErrorC007();
		case RDLConstants.ErrorCodes.C008: return RDL.getI18n().serverErrorC008();
		case RDLConstants.ErrorCodes.C009: return RDL.getI18n().serverErrorC009();
		case RDLConstants.ErrorCodes.C010: return RDL.getI18n().serverErrorC010();
		case RDLConstants.ErrorCodes.C011: return RDL.getI18n().serverErrorC011();
		case RDLConstants.ErrorCodes.C012: return RDL.getI18n().serverErrorC012();
		case RDLConstants.ErrorCodes.C013: return RDL.getI18n().serverErrorC013();
		case RDLConstants.ErrorCodes.C014: return RDL.getI18n().serverErrorC014();
		case RDLConstants.ErrorCodes.C015: return RDL.getI18n().serverErrorC015();
		case RDLConstants.ErrorCodes.C016: return RDL.getI18n().serverErrorC016();
		default: return "Error "+errorCode;
		}
	}
}
