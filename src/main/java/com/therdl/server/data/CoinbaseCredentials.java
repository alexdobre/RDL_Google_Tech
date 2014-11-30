package com.therdl.server.data;

/**
 * Bean to store the CoinbaseCredentials retrieved from the database
 */
public class CoinbaseCredentials {

	private String clientId;
	private String clientSecret;

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


}
