package com.therdl.server.paypal_payment;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.paypal.exception.*;
import com.paypal.sdk.exceptions.OAuthException;
import com.therdl.server.api.UserService;
import com.therdl.server.util.ServerUtils;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileReq;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileRequestType;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.*;

import javax.inject.Provider;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// # CreateRecurringPaymentsProfile API
// The CreateRecurringPaymentsProfile API operation creates a recurring
// payments profile.
// You must invoke the CreateRecurringPaymentsProfile API operation for each
// profile you want to create. The API operation creates a profile and an
// associated billing agreement.
// `Note:
// There is a one-to-one correspondence between billing agreements and
// recurring payments profiles. To associate a recurring payments profile
// with its billing agreement, you must ensure that the description in the
// recurring payments profile matches the description of a billing
// agreement. For version 54.0 and later, use SetExpressCheckout to initiate
// creation of a billing agreement.`
// This sample code uses Merchant Java SDK to make API call. You can
// download the SDKs [here](https://github.com/paypal/sdk-packages/tree/gh-pages/merchant-sdk/java)
public class CreateRecurringPaymentsProfile {

	final Logger logger = LoggerFactory.getLogger(CreateRecurringPaymentsProfile.class);

	private GetRecurringPaymentsProfileDetails getRecurringPaymentsProfileDetails = new GetRecurringPaymentsProfileDetails();
	public static final long MINUTE = 60 * 1000; // in milli-seconds.
	private Date oldDate = new Date();
	int frequency = 1;
	int failedBillingAttempts = 5;

	private Beanery beanery = AutoBeanFactorySource.create(Beanery.class);

	public CreateRecurringPaymentsProfile() {

	}

	public CreateRecurringPaymentsProfileResponseType createRecurringPayment(String token, Provider<HttpSession> session, UserService userService) {

		// ## CreateRecurringPaymentsProfileReq
		CreateRecurringPaymentsProfileReq createRecurringPaymentsProfileReq = new CreateRecurringPaymentsProfileReq();
		CreateRecurringPaymentsProfileRequestType createRecurringPaymentsProfileRequest = new CreateRecurringPaymentsProfileRequestType();
		//        createRecurringPaymentsProfileRequest.setVersion(PayPalConstants.PAYPAL_API_VERSION);
		// You can include up to 10 recurring payments profiles per request. The
		// order of the profile details must match the order of the billing
		// agreement details specified in the SetExpressCheckout request which
		// takes mandatory argument:
		//
		// * `billing start date` - The date when billing for this profile begins.
		// `Note:
		// The profile may take up to 24 hours for activation.`
		Date billingStartDate = new Date(oldDate.getTime() + 1 * MINUTE);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String recurringBillingStartDate = format1.format(billingStartDate);

		RecurringPaymentsProfileDetailsType recurringPaymentsProfileDetails = new RecurringPaymentsProfileDetailsType(recurringBillingStartDate);

		// Billing amount for each billing cycle during this payment period.
		// This amount does not include shipping and tax amounts.
		// `Note:
		// All amounts in the CreateRecurringPaymentsProfile request must have
		// the same currency.`
		BasicAmountType billingAmount = new BasicAmountType(
				CurrencyCodeType.USD, PayPalConstants.CHARGE_AMOUNT);

		// Regular payment period for this schedule which takes mandatory
		// params:
		//
		// * `Billing Period` - Unit for billing during this subscription period. It is one of the
		// following values:
		//  * Day
		//  * Week
		//  * SemiMonth
		//  * Month
		//  * Year
		//  For SemiMonth, billing is done on the 1st and 15th of each month.
		//  `Note:
		//  The combination of BillingPeriod and BillingFrequency cannot exceed
		//  one year.`
		// * `Billing Frequency` - Number of billing periods that make up one billing cycle.
		// The combination of billing frequency and billing period must be less
		// than or equal to one year. For example, if the billing cycle is
		// Month, the maximum value for billing frequency is 12. Similarly, if
		// the billing cycle is Week, the maximum value for billing frequency is
		// 52.
		// `Note:
		// If the billing period is SemiMonth, the billing frequency must be 1.`
		// * `Billing Amount`
		BillingPeriodDetailsType paymentPeriod = new BillingPeriodDetailsType(
				PayPalConstants.BILLING_PERIOD, frequency, billingAmount); //monthly payment

		//(Optional) Information about activating a profile, such as whether there is an initial non-recurring payment amount
		// immediately due upon profile creation and how to override a pending profile PayPal suspends when the initial
		// payment amount fails.
		//        ActivationDetailsType activationDetailsType = new ActivationDetailsType();
		//        activationDetailsType.setInitialAmount(billingAmount);
		//        activationDetailsType.setFailedInitialAmountAction(FailedPaymentActionType.CONTINUEONFAILURE);

		// Describes the recurring payments schedule, including the regular
		// payment period, whether there is a trial period, and the number of
		// payments that can fail before a profile is suspended which takes
		// mandatory params:
		//
		// * `Description` - Description of the recurring payment.
		// `Note:
		// You must ensure that this field matches the corresponding billing
		// agreement description included in the SetExpressCheckout request.`
		// * `Payment Period`
		ScheduleDetailsType scheduleDetails = new ScheduleDetailsType();
		scheduleDetails.setDescription(PayPalConstants.BILLING_AGREEMENT_DESCRIPTION);
		scheduleDetails.setPaymentPeriod(paymentPeriod);
		scheduleDetails.setAutoBillOutstandingAmount(AutoBillType.ADDTONEXTBILLING);
		scheduleDetails.setMaxFailedPayments(failedBillingAttempts);
		//        scheduleDetails.setActivationDetails(activationDetailsType);

		// `CreateRecurringPaymentsProfileRequestDetailsType` which takes
		// mandatory params:
		//
		// * `Recurring Payments Profile Details`
		// * `Schedule Details`
		CreateRecurringPaymentsProfileRequestDetailsType createRecurringPaymentsProfileRequestDetails = new CreateRecurringPaymentsProfileRequestDetailsType(
				recurringPaymentsProfileDetails, scheduleDetails);

		// Either EC token or a credit card number is required.If you include
		// both token and credit card number, the token is used and credit card number is
		// ignored
		// In case of setting EC token,
		// `createRecurringPaymentsProfileRequestDetails.setToken("EC-5KH01765D1724703R");`
		// A timestamped token, the value of which was returned in the response
		// to the first call to SetExpressCheckout. Call
		// CreateRecurringPaymentsProfile once for each billing
		// agreement included in SetExpressCheckout request and use the same
		// token for each call. Each CreateRecurringPaymentsProfile request
		// creates a single recurring payments profile.
		// `Note:
		// Tokens expire after approximately 3 hours.`
		createRecurringPaymentsProfileRequestDetails.setToken(token);

		// Credit card information for recurring payments using direct payments.
		//		CreditCardDetailsType creditCard = new CreditCardDetailsType();

		// Type of credit card. For UK, only Maestro, MasterCard, Discover, and
		// Visa are allowable. For Canada, only MasterCard and Visa are
		// allowable and Interac debit cards are not supported. It is one of the
		// following values:
		//
		// * Visa
		// * MasterCard
		// * Discover
		// * Amex
		// * Solo
		// * Switch
		// * Maestro: See note.
		// `Note:
		// If the credit card type is Maestro, you must set currencyId to GBP.
		// In addition, you must specify either StartMonth and StartYear or
		// IssueNumber.`
		//		creditCard.setCreditCardType(CreditCardTypeType.VISA);

		// Credit Card Number
		//		creditCard.setCreditCardNumber("4442662639546634");

		// Credit Card Expiration Month
		//		creditCard.setExpMonth(Integer.parseInt("12"));

		// Credit Card Expiration Year
		//		creditCard.setExpYear(Integer.parseInt("2016"));
		//		createRecurringPaymentsProfileRequestDetails.setCreditCard(creditCard);
		//
		createRecurringPaymentsProfileRequest.setCreateRecurringPaymentsProfileRequestDetails(createRecurringPaymentsProfileRequestDetails);
		createRecurringPaymentsProfileReq.setCreateRecurringPaymentsProfileRequest(createRecurringPaymentsProfileRequest);

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalConfiguration.getAcctAndConfig());
		CreateRecurringPaymentsProfileResponseType createRecurringPaymentsProfileResponse = null;
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			createRecurringPaymentsProfileResponse = service
					.createRecurringPaymentsProfile(createRecurringPaymentsProfileReq);
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
		if (createRecurringPaymentsProfileResponse.getAck().getValue()
				.equalsIgnoreCase("success")) {

			// A unique identifier for future reference to the details of
			// this recurring payment.

			String paypalProfileId = createRecurringPaymentsProfileResponse.getCreateRecurringPaymentsProfileResponseDetails().getProfileID();
			logger.info("Profile ID:" + paypalProfileId);

			String userId = (String)session.get().getAttribute("userid");

			logger.info("USER ID : " + userId);
			//update the paypal Id for querying when IPN was established
			UserBean userBean = userService.getUserByEmail(userId);
			userBean.setPaypalId(paypalProfileId);
			ServerUtils.extendTitle(userBean, RDLConstants.UserTitle.RDL_SUPPORTER, beanery);

			userService.updateUser(userBean);

		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			List<ErrorType> errorList = createRecurringPaymentsProfileResponse
					.getErrors();
			logger.error("API Error Message : "
					+ errorList.get(0).getLongMessage());
		}
		return createRecurringPaymentsProfileResponse;
	}
}
