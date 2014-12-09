package com.therdl.server.paypal_payment;

import com.paypal.exception.*;
import com.paypal.sdk.exceptions.OAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import urn.ebay.api.PayPalAPI.GetRecurringPaymentsProfileDetailsReq;
import urn.ebay.api.PayPalAPI.GetRecurringPaymentsProfileDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetRecurringPaymentsProfileDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
* # GetRecurringPaymentsProfileDetails API
* Obtain information about a recurring payments profile.
* download the SDKs [here](https://github.com/paypal/sdk-packages/tree/gh-pages/merchant-sdk/java)
*/
public class GetRecurringPaymentsProfileDetails {
	final Logger logger = LoggerFactory.getLogger(GetRecurringPaymentsProfileDetails.class);

	public GetRecurringPaymentsProfileDetails() {

	}

	public GetRecurringPaymentsProfileDetailsResponseType getRecurringPaymentsProfileDetails(String profileId) {

		// ## GetRecurringPaymentsProfileDetails
		GetRecurringPaymentsProfileDetailsReq getRecurringPaymentsProfileDetailsReq = new GetRecurringPaymentsProfileDetailsReq();
		GetRecurringPaymentsProfileDetailsRequestType getRecurringPaymentsProfileDetailsRequestType = new GetRecurringPaymentsProfileDetailsRequestType();

		getRecurringPaymentsProfileDetailsRequestType.setProfileID(profileId);

		getRecurringPaymentsProfileDetailsReq.setGetRecurringPaymentsProfileDetailsRequest(getRecurringPaymentsProfileDetailsRequestType);

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalConfiguration.getAcctAndConfig());
		GetRecurringPaymentsProfileDetailsResponseType getRecurringPaymentsProfileDetailsResponseDetailsType = null;
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			getRecurringPaymentsProfileDetailsResponseDetailsType = service.getRecurringPaymentsProfileDetails(getRecurringPaymentsProfileDetailsReq);
		} catch (SSLConfigurationException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (InvalidCredentialException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (HttpErrorException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (InvalidResponseDataException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (ClientActionRequiredException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (MissingCredentialException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (OAuthException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (ParserConfigurationException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (SAXException e) {
			logger.error("Error Message : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Error Message : " + e.getMessage());
		}

		return getRecurringPaymentsProfileDetailsResponseDetailsType;
	}
}
