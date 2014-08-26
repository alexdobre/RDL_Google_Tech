package com.therdl.server.paypal_payment;

import java.io.IOException;

import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.therdl.server.api.UserService;
import com.therdl.shared.Constants;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileResponseType;

@Singleton
public class PaypalSubscriptionCallbackServlet extends HttpServlet {
	final Logger log = LoggerFactory.getLogger(PaypalSubscriptionCallbackServlet.class);

	private static final long serialVersionUID = 1L;

	private Provider<HttpSession> session;
	private UserService userService;
	private CreateRecurringPaymentsProfile createRecurringPaymentsProfile = new CreateRecurringPaymentsProfile();

	@Inject
	public PaypalSubscriptionCallbackServlet(Provider<HttpSession> session, UserService userService) {
		this.session = session;
		this.userService = userService;
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

		String token = request.getParameter("token");

		CreateRecurringPaymentsProfileResponseType createRecurringPaymentsProfileResponseType = createRecurringPaymentsProfile.createRecurringPayment(token, session, userService);

		if (createRecurringPaymentsProfileResponseType.getAck().getValue()
				.equalsIgnoreCase("success")) {
			response.sendRedirect(url.toString() + Constants.MAIN_PAGE);
		} else {
			response.sendRedirect(url.toString() + Constants.ERROR_PAGE);
		}
	}
}