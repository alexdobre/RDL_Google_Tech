package com.therdl.server.validator.impl;

import org.mindrot.jbcrypt.BCrypt;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.server.validator.TokenValidator;
import com.therdl.server.validator.UserValidator;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.TokenInvalidException;
import com.therdl.shared.exceptions.UserValidationException;

/**
 * Holds validation for the user
 */
@Singleton
public class UserValidatorImpl implements UserValidator {

	private TokenValidator tokenValidator;

	@Inject
	public UserValidatorImpl(TokenValidator tokenValidator) {
		this.tokenValidator = tokenValidator;
	}

	public UserBean validateCanChangePass(AutoBean<AuthUserBean> authBean)
			throws UserValidationException, TokenInvalidException {
		UserBean userBean = tokenValidator.validateTokenViaUsername(authBean.as().getName(), authBean.as().getToken());
		//we check the old pass
		if (!BCrypt.checkpw(authBean.as().getOldPass(), userBean.getPassHash())) {
			throw new UserValidationException(RDLConstants.ErrorCodes.C001);
		}
		return userBean;
	}
}
