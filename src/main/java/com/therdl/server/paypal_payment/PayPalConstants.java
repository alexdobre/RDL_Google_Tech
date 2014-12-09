package com.therdl.server.paypal_payment;

import urn.ebay.apis.eBLBaseComponents.BillingPeriodType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

public class PayPalConstants {

    public static final String CURRENCY = "CURRENCY";
    public static final String CHARGE_AMOUNT_USD = "7.00";
    public static final String CHARGE_AMOUNT_GBP = "5.00";
    public static final String CHARGE_AMOUNT_EUR = "6.00";
    public static final BillingPeriodType BILLING_PERIOD = BillingPeriodType.MONTH;
    public static final String BILLING_AGREEMENT_DESCRIPTION_USD = "User becomes RDL supporter at \"" + CurrencyCodeType.USD + PayPalConstants.CHARGE_AMOUNT_USD + "\" per " + BILLING_PERIOD.name();
    public static final String BILLING_AGREEMENT_DESCRIPTION_GBP = "User becomes RDL supporter at \"" + CurrencyCodeType.GBP + PayPalConstants.CHARGE_AMOUNT_GBP + "\" per " + BILLING_PERIOD.name();
    public static final String BILLING_AGREEMENT_DESCRIPTION_EUR = "User becomes RDL supporter at \"" + CurrencyCodeType.EUR + PayPalConstants.CHARGE_AMOUNT_EUR + "\" per " + BILLING_PERIOD.name();
    public static final String PAYPAL_RETURN_URL = "/rdl/GetExpressCheckout";
    public static final String PAYPAL_CANCEL_URL = "/rdl.html";
    public static final String PAYPAL_IPN_NOTIFY_URL = "/rdl/ipn";
    public static final String PAYPAL_CHECKOUT_URL = "/rdl/paypalCheckout";
    public static final String PAYPAL_SANDBOX_ACCOUNT_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=";
    public static final String PAYPAL_PRODUCTION_ACCOUNT_URL = "https://www.paypal.com/webscr&cmd=_express-checkout&token=";
    public static final String SANDBOX_MODE = "sandbox";
    public static final String LIVE_MODE = "live";
}
