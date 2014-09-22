package com.therdl.server.paypal_payment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.therdl.shared.Constants;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;

@Singleton
public class PaypalSubscriptionServlet extends HttpServlet {
	final Logger log = LoggerFactory.getLogger(PaypalSubscriptionServlet.class);

	private static final long serialVersionUID = 1L;

	private SetExpressCheckout setExpressCheckout = new SetExpressCheckout();
	private PayPalConfiguration paypalConfiguration;

	@Inject
	public PaypalSubscriptionServlet(PayPalConfiguration paypalConfiguration) {
		//to make sure paypal credentials are initialized
		log.info("PaypalConfig - INIT");
		this.paypalConfiguration = paypalConfiguration;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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