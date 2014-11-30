package com.therdl.server.bitcoin_payment;

import com.google.inject.Singleton;
import com.therdl.server.apiimpl.CredentialsService;
import com.therdl.server.data.CoinbaseCredentials;

import javax.inject.Inject;

@Singleton
public class CoinbaseConfiguration {

	private CredentialsService credentialsService;
	private static CoinbaseCredentials coinbaseCredentials;

	@Inject
	public CoinbaseConfiguration(CredentialsService credentialsService) {
		this.credentialsService = credentialsService;
		coinbaseCredentials = credentialsService.getCoinBaseCredentials();
	}


	public static CoinbaseCredentials getCoinbaseCredentials() {
		return coinbaseCredentials;
	}
}
