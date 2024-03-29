package com.therdl.server.paypal_payment;

import com.paypal.exception.*;
import com.paypal.sdk.exceptions.OAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import urn.ebay.api.PayPalAPI.*;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//# DoExpressCheckout API
// The DoExpressCheckoutPayment API operation completes an Express Checkout
// transaction. If you set up a billing agreement in your SetExpressCheckout
// API call, the billing agreement is created when you call the
// DoExpressCheckoutPayment API operation.
// This sample code uses Merchant Java SDK to make API call. You can
// download the SDKs [here](https://github.com/paypal/sdk-packages/tree/gh-pages/merchant-sdk/java)
public class DoExpressCheckout {

	final Logger logger = LoggerFactory.getLogger(DoExpressCheckout.class);

	public DoExpressCheckout() {

	}

	public DoExpressCheckoutPaymentResponseType doExpressCheckout(GetExpressCheckoutDetailsResponseType getExpressCheckoutDetailsResponseType, String url) {

		String token = getExpressCheckoutDetailsResponseType.getGetExpressCheckoutDetailsResponseDetails().getToken();
		String payerId = getExpressCheckoutDetailsResponseType.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID();

		// ## DoExpressCheckoutPaymentReq
		DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();

		DoExpressCheckoutPaymentRequestDetailsType doExpressCheckoutPaymentRequestDetails = new DoExpressCheckoutPaymentRequestDetailsType();

		// The timestamped token value that was returned in the
		// `SetExpressCheckout` response and passed in the
		// `GetExpressCheckoutDetails` request.
		doExpressCheckoutPaymentRequestDetails.setToken(token);

		// Unique paypal buyer account identification number as returned in
		// `GetExpressCheckoutDetails` Response
		doExpressCheckoutPaymentRequestDetails.setPayerID(payerId);

		// ### Payment Information
		// list of information about the payment
		List<PaymentDetailsType> paymentDetailsList = new ArrayList<PaymentDetailsType>();

		// information about the first payment
		PaymentDetailsType paymentDetails1 = new PaymentDetailsType();

		// Total cost of the transaction to the buyer. If shipping cost and tax
		// charges are known, include them in this value. If not, this value
		// should be the current sub-total of the order.
		//
		// If the transaction includes one or more one-time purchases, this field must be equal to
		// the sum of the purchases. Set this field to 0 if the transaction does
		// not include a one-time purchase such as when you set up a billing
		// agreement for a recurring payment that is not immediately charged.
		// When the field is set to 0, purchase-specific fields are ignored.
		//
		// * `Currency Code` - You must set the currencyID attribute to one of the
		// 3-character currency codes for any of the supported PayPal
		// currencies.
		// * `Amount`

		BasicAmountType orderTotal1 = new BasicAmountType(CurrencyCodeType.USD,
				PayPalConstants.CHARGE_AMOUNT_USD);
		paymentDetails1.setOrderTotal(orderTotal1);
		paymentDetails1.setButtonSource("PayPal_SDK");

		// How you want to obtain payment. When implementing parallel payments,
		// this field is required and must be set to `Order`. When implementing
		// digital goods, this field is required and must be set to `Sale`. If the
		// transaction does not include a one-time purchase, this field is
		// ignored. It is one of the following values:
		//
		// * `Sale` - This is a final sale for which you are requesting payment
		// (default).
		// * `Authorization` - This payment is a basic authorization subject to
		// settlement with PayPal Authorization and Capture.
		// * `Order` - This payment is an order authorization subject to
		// settlement with PayPal Authorization and Capture.
		// Note:
		// You cannot set this field to Sale in SetExpressCheckout request and
		// then change the value to Authorization or Order in the
		// DoExpressCheckoutPayment request. If you set the field to
		// Authorization or Order in SetExpressCheckout, you may set the field
		// to Sale.
		paymentDetails1.setPaymentAction(PaymentActionCodeType.SALE);

		// Unique identifier for the merchant. For parallel payments, this field
		// is required and must contain the Payer Id or the email address of the
		// merchant.
		//		SellerDetailsType sellerDetails1 = new SellerDetailsType();
		//		sellerDetails1.setPayPalAccountID(PayPalConstants.PAYPAL_ACCOUNT_ID);
		//		paymentDetails1.setSellerDetails(sellerDetails1);

		// A unique identifier of the specific payment request, which is
		// required for parallel payments.
		//        paymentDetails1.setPaymentRequestID(PayPalConstants.PAYMENT_REQUEST_ID);

		// Your URL for receiving Instant Payment Notification (IPN) about this transaction. If you do not specify this value in the request, the notification URL from your Merchant Profile is used, if one exists.
		paymentDetails1.setNotifyURL(url + PayPalConstants.PAYPAL_IPN_NOTIFY_URL);

		paymentDetailsList.add(paymentDetails1);
		doExpressCheckoutPaymentRequestDetails.setPaymentDetails(paymentDetailsList);

		DoExpressCheckoutPaymentRequestType doExpressCheckoutPaymentRequest = new DoExpressCheckoutPaymentRequestType(
				doExpressCheckoutPaymentRequestDetails);

		doExpressCheckoutPaymentReq
				.setDoExpressCheckoutPaymentRequest(doExpressCheckoutPaymentRequest);

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalConfiguration.getAcctAndConfig());
		DoExpressCheckoutPaymentResponseType doExpressCheckoutPaymentResponse = null;
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			doExpressCheckoutPaymentResponse = service
					.doExpressCheckoutPayment(doExpressCheckoutPaymentReq);
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

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (doExpressCheckoutPaymentResponse.getAck().getValue()
				.equalsIgnoreCase("success")) {

			// Transaction identification number of the transaction that was
			// created.
			// This field is only returned after a successful transaction
			// for DoExpressCheckout has occurred.
			if (doExpressCheckoutPaymentResponse
					.getDoExpressCheckoutPaymentResponseDetails()
					.getPaymentInfo() != null) {
				Iterator<PaymentInfoType> paymentInfoIterator = doExpressCheckoutPaymentResponse
						.getDoExpressCheckoutPaymentResponseDetails()
						.getPaymentInfo().iterator();
				while (paymentInfoIterator.hasNext()) {
					PaymentInfoType paymentInfo = paymentInfoIterator
							.next();
					logger.info("Transaction ID : "
							+ paymentInfo.getTransactionID());
				}
			}
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			List<ErrorType> errorList = doExpressCheckoutPaymentResponse
					.getErrors();
			logger.error("API Error Message : "
					+ errorList.get(0).getLongMessage());
		}
		return doExpressCheckoutPaymentResponse;
	}
}
