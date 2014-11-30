package com.therdl.server.bitcoin_payment;


public class CoinbasePaymentConstants {

    public static final String COINBASE_API_KEY = "JIvn1qEd1yMfy4sk";
    public static final String COINBASE_API_SECRET = "c0H3CuxI1ipX0R5AcxpEvosNaM9KKFsM";

    public static final String COINBASE_CREATE_PROFILE_URL = "/rdl/coinbaseCreateProfile";
    public static final String COINBASE_CHECKOUT__URL = "/rdl/coinbaseRequest";
    public static final String COINBASE_RECURRING_PAYMENT_URL = "/rdl/coinbaseRecurringPayment";
    public static final String COINBASE_CALLBACK_URL = "/rdl/coinbaseCallback";

    public static final String COINBASE_LIVE_URL = "https://54.191.161.185";
    public static final String COINBASE_DEV_URL = "https://127.0.0.1:8080";

    public static final String COINBASE_SCOPE = "user+recurring_payments+request+balance+buttons";
    public static final String COINBASE_FULL_CALLBACK_URL = COINBASE_DEV_URL + COINBASE_CALLBACK_URL;
    public static final String COINBASE_AUTHORIZE_URL = "https://www.coinbase.com/oauth/authorize";
    public static final String COINBASE_REQUEST_TOKEN_ENDPOINT_URL = "https://www.coinbase.com/oauth/token"; // endpoint for requesting tokens
}
