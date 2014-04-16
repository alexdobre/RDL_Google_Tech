package com.therdl.server.paypal_payment;

public class PayPalIPNVariables {
    //Payment received for a single item; source is Express Checkout
    public static String EXPRESS_CHECKOUT = "express_checkout";
    //Recurring payment received
    public static String RECURRING_PAYMENT = "recurring_payment";
    //Recurring payment profile created
    public static String RECURRING_PAYMENT_PROFILE_CREATED = "recurring_payment_profile_created";
    //Recurring payment profile canceled
    public static String RECURRING_PAYMENT_PROFILE_CANCEL = "recurring_payment_profile_cancel";
    //Recurring payment suspended
    //This transaction type is sent if PayPal tried to collect a recurring payment,
    //but the related recurring payments profile has been suspended.
    public static String RECURRING_PAYMENT_SUSPENDED = "recurring_payment_suspended";
    //Recurring payment expired
    public static String RECURRING_PAYMENT_EXPIRED = "recurring_payment_expired";
    //Recurring payment failed and the related recurring payment profile has been suspended
    //This transaction type is sent if:
    //PayPal's attempt to collect a recurring payment failed
    //The "max failed payments" setting in the customer's recurring payment profile is 1 or greater
    //the number of attempts to collect payment has exceeded the value specified for "max failed payments"
    //In this case, PayPal suspends the customer's recurring payment profile.
    public static String RECURRING_PAYMENT_SUSPENDED_DUE_TO_MAX_FAILED_PAYMENT = "recurring_payment_suspended_due_to_max_failed_payment";
    //Recurring payment ID
    public static String RECURRING_PAYMENT_ID = "recurring_payment_id";
    //When a recurring payment was created
    public static String TIME_CREATED = "time_created";
    //Next payment date for a recurring payment
    public static String NEXT_PAYMENT_DATE = "next_payment_date";


}
