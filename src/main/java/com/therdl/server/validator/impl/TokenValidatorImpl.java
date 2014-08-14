package com.therdl.server.validator.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.therdl.server.api.UserService;
import com.therdl.server.validator.TokenValidator;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.TokenInvalidException;

/**
 * Validates a token
 */
@Singleton
public class TokenValidatorImpl implements TokenValidator {

	private SecureRandom random = new SecureRandom();
	private UserService userService;

	@Inject
	public TokenValidatorImpl(UserService userService) {
		this.userService = userService;
	}

	public String createToken() {
		return new BigInteger(130, random).toString(32);
	}

	public void validateTokenViaUsername(String username, String token) throws TokenInvalidException {
		UserBean userBean = userService.getUserByUsername(username);
		if (!token.equals(userBean.getToken())){
			throw new TokenInvalidException();
		}
	}

	public void validateTokenViaEmail(String email, String token) throws TokenInvalidException {
		UserBean userBean = userService.getUserByEmail(email);
		if (!token.equals(userBean.getToken())){
			throw new TokenInvalidException();
		}
	}

	public void validateTokenViaId(String id, String token) throws TokenInvalidException {
		UserBean userBean = userService.getUserById(id);
		if (!token.equals(userBean.getToken())){
			throw new TokenInvalidException();
		}
	}

}
