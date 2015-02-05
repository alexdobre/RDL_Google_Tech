package com.therdl.client.validation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.RDL;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.AuthUserBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Validation methods for the user
 */
public class UserViewValidator {

	private static Map<String, String> errorMessageMap;

	static {
		errorMessageMap = new HashMap<String, String>();
		errorMessageMap.put("email", RDL.getI18n().formErrorEmailFormat());
		errorMessageMap.put("password", RDL.getI18n().formErrorPasswordFormat());
		errorMessageMap.put("name", RDL.getI18n().formErrorUsernameFormat());
		}

	public static String validateAuthUserBean(AutoBean<AuthUserBean> bean) {
		Log.info("Validating AuthUserBean: " + AutoBeanCodex.encode(bean).getPayload());
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<AuthUserBean>> violations = validator.validate(bean.as());
		if (violations.iterator().hasNext()) {
			ConstraintViolation violation = violations.iterator().next();
			Log.info("Bean invalid: " + violation.getPropertyPath() + " " + violation.getMessage());
			return errorMessageMap.get(violation.getPropertyPath().toString());
		}
		if (!bean.as().getPassword().equals(bean.as().getPasswordConfirm())) {
			return RDL.getI18n().passwordsDoNotMatch();
		}
		Log.info("Bean valid");
		return null;
	}

	public static String validateChangePassInput(String oldPass, String newPass, String newPassConfirm) {
		//user input cannot be empty
		if (RDLUtils.isEmpty(oldPass) || RDLUtils.isEmpty(newPass) || RDLUtils.isEmpty(newPassConfirm)) {
			return RDL.getI18n().formErrorPleaseProvideInput();
		}

		if (!newPass.equals(newPassConfirm)) {
			return RDL.getI18n().formErrorPassEqual();
		}

		//min 8 char
		if (newPass.length() < 8) {
			return RDL.getI18n().formErrorPassShort();
		}

		return null;
	}

}
