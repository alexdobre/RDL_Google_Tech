package com.therdl.server.data;

/**
 * Bean to store the PaypalCredentials retrieved from the database
 */
public class PaypalCredentials {

	private String username;
	private String password;
	private String signature;
	private String mode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
