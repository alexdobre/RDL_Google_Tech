package com.therdl.server.paypal_payment;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

public class PayPalConstants {

	public static final String CHARGE_AMOUNT = "1.00";
	public static final String BILLING_AGREEMENT_DESCRIPTION = "Buyer is billed at \"" + CurrencyCodeType.USD + PayPalConstants.CHARGE_AMOUNT + "\" per month";
	public static final String PAYPAL_RETURN_URL = "/rdl/GetExpressCheckout";
	public static final String PAYPAL_CANCEL_URL = "/rdl.html";
	public static final String PAYPAL_IPN_NOTIFY_URL = "/rdl/ipn";
	public static final String PAYPAL_CHECKOUT_URL = "/rdl/paypalCheckout";
	public static final String PAYPAL_SANDBOX_ACCOUNT_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=";
	public static final String PAYPAL_PRODUCTION_ACCOUNT_URL = "https://www.paypal.com/webscr&cmd=";
}
