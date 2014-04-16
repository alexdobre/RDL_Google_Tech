package com.therdl.server.paypal_payment;

import com.google.inject.Singleton;
import com.therdl.shared.Constants;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class PaypalSubscriptionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SetExpressCheckout setExpressCheckout = new SetExpressCheckout();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().toString());

        StringBuffer url = new StringBuffer();
        url.append("http://");
        url.append(request.getServerName());
        url.append(":");
        url.append(request.getServerPort());
        url.append(request.getContextPath());

        SetExpressCheckoutResponseType setExpressCheckoutResponseType = setExpressCheckout.setExpressCheckout(request, response, url.toString());

        if (setExpressCheckoutResponseType.getAck().getValue()
                .equalsIgnoreCase("failure")) {
            response.sendRedirect(url.toString() + Constants.ERROR_PAGE);
        }

    }


}