package com.therdl.server.restapi;
import com.google.inject.Singleton;
import com.therdl.server.api.PaymentService;
import com.therdl.server.api.UserService;
import com.therdl.server.data.PaypalCredentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Processes the IPN messages from Paypal and updates the user's title accordingly
 * See: https://developer.paypal.com/webapps/developer/docs/classic/products/instant-payment-notification/
 */
@Singleton
public class IpnServlet extends HttpServlet  {

    private static final long serialVersionUID = -292632340436154976L;
    static final String NotifyValidate = "cmd=_notify-validate";

    private PaymentService paymentService;
    private UserService userService;

    private final PaypalCredentials paypalCredentials;

    @Inject
    public IpnServlet( UserService userService, PaymentService paymentService) {
        this.paymentService = paymentService;
        this.userService = userService;

        paypalCredentials = paymentService.getPaypalCredentials("ipn");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String str = NotifyValidate;

        Enumeration en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String paramName = (String) en.nextElement();
            String paramValue = request.getParameter(paramName);
            str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");
        }

        //  Post back to PayPal system to validate
        //  NOTE: change http: to https: in the following URL to verify using SSL (for increased security).
        //  using HTTPS requires either Java 1.4 or greater, or Java Secure Socket Extension (JSSE) and configured for older versions.
        URL u = new URL(paypalCredentials.getUrl());
        URLConnection uc = u.openConnection();
        uc.setDoOutput(true);
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        PrintWriter pw = new PrintWriter(uc.getOutputStream());
        pw.println(str);
        pw.close();

        //Read response
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String res = in.readLine();
        in.close();

        // assign posted variables to local variables

        String itemName = request.getParameter("item_name");
        String itemNumber = request.getParameter("item_number");
        String paymentStatus = request.getParameter("payment_status");
        String paymentAmount = request.getParameter("mc_gross");
        String paymentCurrency = request.getParameter("mc_currency");
        String txnId = request.getParameter("txn_id");
        String receiverEmail = request.getParameter("receiver_email");
        String payerEmail = request.getParameter("payer_email");

        //check notification validation
        if (res.equals("VERIFIED")) {
            // check that paymentStatus=Completed
            if (itemName.equals("WordTiller Report") && itemNumber.equals("001")) {

                if (paymentStatus.equals("Completed")) {
                    // check that txnId has not been previously processed
                    // check that receiverEmail is your Primary PayPal email
                    // check that paymentAmount/paymentCurrency are correct
                    // process payment
                } else {
                    if (paymentStatus.equals("Refunded")) {
                    }
                }
            } else {
                // log for investigation
            }
        } else if (res.equals("INVALID")) {
            // log for investigation
        } else {
            // error
        }

    }

}
