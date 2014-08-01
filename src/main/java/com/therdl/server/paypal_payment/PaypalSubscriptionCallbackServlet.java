package com.therdl.server.paypal_payment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.therdl.server.api.UserService;
import com.therdl.shared.Constants;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileResponseType;

import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class PaypalSubscriptionCallbackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Provider<HttpSession> session;
	private UserService userService;
	private GetExpressCheckout getExpressCheckout = new GetExpressCheckout();
	private DoExpressCheckout doExpressCheckout = new DoExpressCheckout();
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
		Logger logger = Logger.getLogger(this.getClass().toString());

		StringBuffer url = new StringBuffer();
		url.append("http://");
		url.append(request.getServerName());
		url.append(":");
		url.append(request.getServerPort());
		url.append(request.getContextPath());

		String token = request.getParameter("token");

//        GetExpressCheckoutDetailsResponseType getExpressCheckoutDetailsResponseType = getExpressCheckout.getExpressCheckout(token);
//
//        if (getExpressCheckoutDetailsResponseType.getAck().getValue()
//                .equalsIgnoreCase("failure")) {
//            response.sendRedirect(url.toString() + Constants.ERROR_PAGE);
//        }

		//we want the user to paid immediately.
//        DoExpressCheckoutPaymentResponseType doExpressCheckoutPaymentResponseType = doExpressCheckout.doExpressCheckout(getExpressCheckoutDetailsResponseType, url.toString());
//
//        if (doExpressCheckoutPaymentResponseType.getAck().getValue()
//                .equalsIgnoreCase("failure")) {
//            response.sendRedirect(url.toString() + Constants.ERROR_PAGE);
//        }

		CreateRecurringPaymentsProfileResponseType createRecurringPaymentsProfileResponseType = createRecurringPaymentsProfile.createRecurringPayment(token, session, userService);

		if (createRecurringPaymentsProfileResponseType.getAck().getValue()
				.equalsIgnoreCase("success")) {
			response.sendRedirect(url.toString() + Constants.MAIN_PAGE);
		} else {
			response.sendRedirect(url.toString() + Constants.ERROR_PAGE);
		}


	}


}