package com.therdl.server.data;

/**
 * 
 * Bean to store the social network credentials and keys retrieved from the database
 *
 */
public class SocialNetworkCredentials {
	
	
	/**
	 * name of the social network 
	 */
	private String name;
	
	private String apiKey;
	
	private String secretKey;
	
	
	private String username;
	
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

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

}
