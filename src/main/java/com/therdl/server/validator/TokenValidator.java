package com.therdl.server.validator;

import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.TokenInvalidException;

/**
 * Logic for token validation
 */
public interface TokenValidator {

	/**
	 * Add a random token for the given user, generated at login
	 */
	public String createToken();

	public UserBean validateTokenViaUsername (String username, String token) throws TokenInvalidException;

	public UserBean validateTokenViaEmail (String email, String token) throws TokenInvalidException;

	public UserBean validateTokenViaId (String id, String token) throws TokenInvalidException;
}
