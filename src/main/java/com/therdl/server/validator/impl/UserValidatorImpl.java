package com.therdl.server.validator.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.therdl.server.api.UserService;
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
	private UserService userService;

	@Inject
	public UserValidatorImpl(TokenValidator tokenValidator, UserService userService) {
		this.tokenValidator = tokenValidator;
		this.userService = userService;
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

	public void validateAuthUserBean (AutoBean<AuthUserBean> authBean)throws UserValidationException{
		boolean isValid = true;
		if (authBean != null && authBean.as() != null ) {
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<AuthUserBean>> violations = validator.validate(authBean.as());
			if (!violations.isEmpty()) {
				isValid = false;
			}else {
				//username must be unique
				if (userService.getUserByUsername(authBean.as().getName()) != null){
					throw new UserValidationException(RDLConstants.ErrorCodes.C002);
				}
				//email must be unique
				if (userService.getUserByEmail(authBean.as().getEmail()) != null){
					throw new UserValidationException(RDLConstants.ErrorCodes.C005);
				}
			}
		} else {
			isValid = false;
		}
		if (!isValid){
			throw  new UserValidationException(RDLConstants.ErrorCodes.GENERIC);
		}
	}
}
