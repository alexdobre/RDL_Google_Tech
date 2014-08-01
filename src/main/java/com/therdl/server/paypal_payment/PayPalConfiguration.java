package com.therdl.server.paypal_payment;

import java.util.HashMap;
import java.util.Map;

/**
 * For a full list of configuration parameters refer in wiki page.(https://github.com/paypal/sdk-core-java/wiki/SDK-Configuration-Parameters)
 */
public class PayPalConfiguration {

	// Creates a configuration map containing credentials and other required configuration parameters.
	public static final Map<String, String> getAcctAndConfig() {
		Map<String, String> configMap = new HashMap<String, String>();
		configMap.putAll(getConfig());

		// Account Credential
		configMap.put("acct1.UserName", "markdiesta-facilitator_api1.gmail.com");
		configMap.put("acct1.Password", "1395032487");
		configMap.put("acct1.Signature", "AiPC9BjkCyDFQXbSkoZcgqH3hpacAAhctPJ0aZ9W2usWo1JAF1t0lOGb");

//        configMap.put("acct1.Binding", "SOAP");
//        configMap.put("acct1.EndPoint", "https://api-3t.sandbox.paypal.com/2.0");
//        configMap.put("acct1.RedirectURL", "https://www.sandbox.paypal.com/webscr&cmd=");
//        configMap.put("acct1.DevCentralURL", "https://developer.paypal.com");
//        configMap.put("acct1.IPNEndpoint", "https://www.sandbox.paypal.com/cgi-bin/webscr");

		return configMap;
	}

	public static final Map<String, String> getConfig() {
		Map<String, String> configMap = new HashMap<String, String>();

		// Endpoints are varied depending on whether sandbox OR live is chosen for mode
		configMap.put("mode", "sandbox");


		// These values are defaulted in SDK. If you want to override default values, uncomment it and add your value.
		// configMap.put("http.ConnectionTimeOut", "5000");
		// configMap.put("http.Retry", "2");
		// configMap.put("http.ReadTimeOut", "30000");
		// configMap.put("http.MaxConnection", "100");
		return configMap;
	}
}
