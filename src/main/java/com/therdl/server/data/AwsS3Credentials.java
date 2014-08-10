package com.therdl.server.data;

/**
 * Holds the AwsS3 credentials
 */
public class AwsS3Credentials {

	private String accesKey;
	private String secretKey;

	public String getAccesKey() {
		return accesKey;
	}

	public void setAccesKey(String accesKey) {
		this.accesKey = accesKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
