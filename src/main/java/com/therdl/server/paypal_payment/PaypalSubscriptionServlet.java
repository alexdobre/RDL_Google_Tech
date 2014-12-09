package com.therdl.server.paypal_payment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.therdl.shared.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;

import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class PaypalSubscriptionServlet extends HttpServlet {
	final Logger log = LoggerFactory.getLogger(PaypalSubscriptionServlet.class);

	private static final long serialVersionUID = 1L;

	private SetExpressCheckout setExpressCheckout = new SetExpressCheckout();
	private PayPalConfiguration paypalConfiguration;
	private Provider<HttpSession> session;

	@Inject
	public PaypalSubscriptionServlet(Provider<HttpSession> session, PayPalConfiguration paypalConfiguration) {
		//to make sure paypal credentials are initialized
		log.info("PaypalConfig - INIT");
		this.session = session;
		this.paypalConfiguration = paypalConfiguration;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String currency = request.getParameter("cur");

		request.setAttribute(PayPalConstants.CURRENCY, currency);

		StringBuffer url = new StringBuffer();
		url.append("http://");
		url.append(request.getServerName());
		url.append(":");
		url.append(request.getServerPort());
		url.append(request.getContextPath());

		SetExpressCheckoutResponseType setExpressCheckoutResponseType = setExpressCheckout.setExpressCheckout(request, response, url.toString(), session);

		if (setExpressCheckoutResponseType.getAck().getValue()
				.equalsIgnoreCase("failure")) {
			response.sendRedirect(url.toString() + Constants.ERROR_PAGE);
		}
	}
}