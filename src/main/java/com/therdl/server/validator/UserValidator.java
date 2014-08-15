package com.therdl.server.validator;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.TokenInvalidException;
import com.therdl.shared.exceptions.UserValidationException;

/**
 * Holds validation for the user
 */
public interface UserValidator {

	/**
	 * Checks if the supplied user can change password
	 * @param authBean bean containing the pass change parameters
	 * @return the existing userBean
	 * @throws UserValidationException if validation failed
	 */
	public UserBean validateCanChangePass (AutoBean<AuthUserBean> authBean) throws UserValidationException, TokenInvalidException;
}
