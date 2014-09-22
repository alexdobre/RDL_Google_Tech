package com.therdl.server.paypal_payment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.BillingAgreementDetailsType;
import urn.ebay.apis.eBLBaseComponents.BillingCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

//# SetExpressCheckout API
// The SetExpressCheckout API operation initiates an Express Checkout
// transaction.
// This sample code uses Merchant Java SDK to make API call. You can
// download the SDKs [here](https://github.com/paypal/sdk-packages/tree/gh-pages/merchant-sdk/java)
public class SetExpressCheckout {
	final Logger logger = LoggerFactory.getLogger(SetExpressCheckout.class);

	public SetExpressCheckout() {
	}

	public SetExpressCheckoutResponseType setExpressCheckout(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

		String returnURL = url + PayPalConstants.PAYPAL_RETURN_URL;
		String cancelURL = url + PayPalConstants.PAYPAL_CANCEL_URL;

		// ## SetExpressCheckoutReq
		SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();

		// URL to which the buyer's browser is returned after choosing to pay
		// with PayPal. For digital goods, you must add JavaScript to this page
		// to close the in-context experience.
		// `Note:
		// PayPal recommends that the value be the final review page on which
		// the buyer confirms the order and payment or billing agreement.`
		setExpressCheckoutRequestDetails.setReturnURL(returnURL);

		// URL to which the buyer is returned if the buyer does not approve the
		// use of PayPal to pay you. For digital goods, you must add JavaScript
		// to this page to close the in-context experience.
		// `Note:
		// PayPal recommends that the value be the original page on which the
		// buyer chose to pay with PayPal or establish a billing agreement.`
		setExpressCheckoutRequestDetails.setCancelURL(cancelURL);

		// ### Payment Information
		// list of information about the payment
		List<PaymentDetailsType> paymentDetailsList = new ArrayList<PaymentDetailsType>();

		// information about the first payment
		PaymentDetailsType paymentDetails1 = new PaymentDetailsType();

		// Total cost of the transaction to the buyer. If shipping cost and tax
		// charges are known, include them in this value. If not, this value
		// should be the current sub-total of the order.
		//
		// If the transaction includes one or more one-time purchases, this
		// field must be equal to
		// the sum of the purchases. Set this field to 0 if the transaction does
		// not include a one-time purchase such as when you set up a billing
		// agreement for a recurring payment that is not immediately charged.
		// When the field is set to 0, purchase-specific fields are ignored.
		//
		// * `Currency Code` - You must set the currencyID attribute to one of
		// the
		// 3-character currency codes for any of the supported PayPal
		// currencies.
		// * `Amount`
		BasicAmountType orderTotal1 = new BasicAmountType(CurrencyCodeType.USD, "0"); // set amount to "0" for recurring profile
		paymentDetails1.setOrderTotal(orderTotal1);

		// How you want to obtain payment. When implementing parallel payments,
		// this field is required and must be set to `Order`. When implementing
		// digital goods, this field is required and must be set to `Sale`. If
		// the
		// transaction does not include a one-time purchase, this field is
		// ignored. It is one of the following values:
		//
		// * `Sale` - This is a final sale for which you are requesting payment
		// (default).
		// * `Authorization` - This payment is a basic authorization subject to
		// settlement with PayPal Authorization and Capture.
		// * `Order` - This payment is an order authorization subject to
		// settlement with PayPal Authorization and Capture.
		// `Note:
		// You cannot set this field to Sale in SetExpressCheckout request and
		// then change the value to Authorization or Order in the
		// DoExpressCheckoutPayment request. If you set the field to
		// Authorization or Order in SetExpressCheckout, you may set the field
		// to Sale.`
		//        paymentDetails1.setPaymentAction(PaymentActionCodeType.SALE);

		// Unique identifier for the merchant. For parallel payments, this field
		// is required and must contain the Payer Id or the email address of the
		// merchant.
		//        SellerDetailsType sellerDetails1 = new SellerDetailsType();
		//        sellerDetails1.setPayPalAccountID(PayPalConstants.PAYPAL_ACCOUNT_ID);
		//        paymentDetails1.setSellerDetails(sellerDetails1);

		// A unique identifier of the specific payment request, which is
		// required for parallel payments.
		//        paymentDetails1.setPaymentRequestID(PayPalConstants.PAYMENT_REQUEST_ID);

		// Your URL for receiving Instant Payment Notification (IPN) about this
		// transaction. If you do not specify this value in the request, the
		// notification URL from your Merchant Profile is used, if one exists.
		paymentDetails1.setNotifyURL(url + PayPalConstants.PAYPAL_IPN_NOTIFY_URL);
		paymentDetailsList.add(paymentDetails1);

       	/*
	   	      *  (Required) Type of billing agreement. For recurring payments,
				 *   this field must be set to RecurringPayments.
				 *   In this case, you can specify up to ten billing agreements.
				 *   Other defined values are not valid.
					 Type of billing agreement for reference transactions.
					 You must have permission from PayPal to use this field.
					 This field must be set to one of the following values:
						1. MerchantInitiatedBilling - PayPal creates a billing agreement
						   for each transaction associated with buyer.You must specify
						   version 54.0 or higher to use this option.
						2. MerchantInitiatedBillingSingleAgreement - PayPal creates a
						   single billing agreement for all transactions associated with buyer.
						   Use this value unless you need per-transaction billing agreements.
						   You must specify version 58.0 or higher to use this option.

				 */
		BillingAgreementDetailsType billingAgreement = new BillingAgreementDetailsType(
				BillingCodeType.RECURRINGPAYMENTS);
                /*
                 * Description of goods or services associated with the billing agreement.
				 * This field is required for each recurring payment billing agreement.
				 *  PayPal recommends that the description contain a brief summary of
				 *  the billing agreement terms and conditions. For example,
				 *   buyer is billed at "9.99 per month for 2 years".
				   Character length and limitations: 127 single-byte alphanumeric characters
				 */
		billingAgreement.setBillingAgreementDescription(PayPalConstants.BILLING_AGREEMENT_DESCRIPTION);
		List<BillingAgreementDetailsType> billList = new ArrayList<BillingAgreementDetailsType>();
		billList.add(billingAgreement);
		setExpressCheckoutRequestDetails.setBillingAgreementDetails(billList);

		setExpressCheckoutRequestDetails.setPaymentDetails(paymentDetailsList);

		SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
		SetExpressCheckoutRequestType setExpressCheckoutRequest = new SetExpressCheckoutRequestType(setExpressCheckoutRequestDetails);

		setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequest);

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalConfiguration.getAcctAndConfig());
		SetExpressCheckoutResponseType setExpressCheckoutResponse = null;
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			setExpressCheckoutResponse = service.setExpressCheckout(setExpressCheckoutReq);
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
		if (setExpressCheckoutResponse.getAck().getValue()
				.equalsIgnoreCase("success")) {
			// ### Redirecting to PayPal for authorization
			// Once you get the "Success" response, needs to authorise the
			// transaction by making buyer to login into PayPal. For that,
			// need to construct redirect url using EC token from response.
			// For example,
			// `redirectURL="https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token="+setExpressCheckoutResponse.getToken();`
			if (PayPalConfiguration.getPaypalCredentials().getMode().equals(PayPalConstants.SANDBOX_MODE)){
				response.sendRedirect(PayPalConstants.PAYPAL_SANDBOX_ACCOUNT_URL + setExpressCheckoutResponse.getToken());
			} else {
				response.sendRedirect(PayPalConstants.PAYPAL_PRODUCTION_ACCOUNT_URL + setExpressCheckoutResponse.getToken());
			}
			// Express Checkout Token
			logger.info("EC Token:" + setExpressCheckoutResponse.getToken());
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			List<ErrorType> errorList = setExpressCheckoutResponse.getErrors();
			logger.error("API Error Message : "
					+ errorList.get(0).getLongMessage());


		}
		return setExpressCheckoutResponse;
	}
}
