package com.therdl.shared.beans;

/**
 * This bean contains a security token
 */
public interface TokenizedBean {

	public String getToken();

	public void setToken(String token);
}
