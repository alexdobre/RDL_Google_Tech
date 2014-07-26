package com.therdl.server.paypal_payment;

import com.google.inject.Singleton;
import com.paypal.exception.*;
import com.paypal.sdk.exceptions.OAuthException;
import org.xml.sax.SAXException;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.eBLBaseComponents.ErrorType;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

//# GetExpressCheckout API
// The GetExpressCheckoutDetails API operation obtains information about
// an Express Checkout transaction.
// This sample code uses Merchant Java SDK to make API call. You can
// download the SDKs [here](https://github.com/paypal/sdk-packages/tree/gh-pages/merchant-sdk/java)
@Singleton
public class GetExpressCheckout {

    public GetExpressCheckout() {

    }

    public GetExpressCheckoutDetailsResponseType getExpressCheckout(String token) {

        Logger logger = Logger.getLogger(this.getClass().toString());

        // ## GetExpressCheckoutDetailsReq
        GetExpressCheckoutDetailsReq getExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();

        // A timestamped token, the value of which was returned by
        // `SetExpressCheckout` response.
        GetExpressCheckoutDetailsRequestType getExpressCheckoutDetailsRequest = new GetExpressCheckoutDetailsRequestType(token);

        getExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(getExpressCheckoutDetailsRequest);

        // ## Creating service wrapper object
        // Creating service wrapper object to make API call and loading
        // configuration file for your credentials and endpoint
        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalConfiguration.getAcctAndConfig());
        GetExpressCheckoutDetailsResponseType getExpressCheckoutDetailsResponse = null;
        try {
            // ## Making API call
            // Invoke the appropriate method corresponding to API in service
            // wrapper object
            getExpressCheckoutDetailsResponse = service
                    .getExpressCheckoutDetails(getExpressCheckoutDetailsReq);
        } catch (SSLConfigurationException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (InvalidCredentialException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (HttpErrorException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (InvalidResponseDataException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (ClientActionRequiredException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (MissingCredentialException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (InterruptedException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (OAuthException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (ParserConfigurationException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (SAXException e) {
            logger.severe("Error Message : " + e.getMessage());
        } catch (IOException e) {
            logger.severe("Error Message : " + e.getMessage());
        }

        // ## Accessing response parameters
        // You can access the response parameters using getter methods in
        // response object as shown below
        // ### Success values
        if (getExpressCheckoutDetailsResponse.getAck().getValue()
                .equalsIgnoreCase("success")) {

            // Unique PayPal Customer Account identification number. This
            // value will be null unless you authorize the payment by
            // redirecting to PayPal after `SetExpressCheckout` call.
            logger.info("PayerID : "
                    + getExpressCheckoutDetailsResponse
                    .getGetExpressCheckoutDetailsResponseDetails()
                    .getPayerInfo().getPayerID());

        }
        // ### Error Values
        // Access error values from error list using getter methods
        else {
            List<ErrorType> errorList = getExpressCheckoutDetailsResponse
                    .getErrors();
            logger.severe("API Error Message : "
                    + errorList.get(0).getLongMessage());
        }
        return getExpressCheckoutDetailsResponse;

    }
}