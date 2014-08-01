package com.therdl.server.restapi;

import com.therdl.server.api.PaymentService;
import com.therdl.server.api.UserService;
import com.therdl.server.data.PaypalCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * This servlet responds to paypal's Payment Data Transfer to confirm to the user that a transaction has taken place.
 * It also Updates the user's title in accordance.
 * See: https://developer.paypal.com/docs/classic/products/payment-data-transfer/
 */
public class PdtServlet extends HttpServlet {

	private static final long serialVersionUID = 5944361005662719642L;

	static final String NotifySync = "cmd=_notify-synch";

	private PaymentService paymentService;
	private UserService userService;

	private final PaypalCredentials paypalCredentials;

	public PdtServlet(UserService userService, PaymentService paymentService) {
		this.paymentService = paymentService;
		this.userService = userService;

		paypalCredentials = paymentService.getPaypalCredentials("pdt");
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		String str = NotifySync + "&" + paypalCredentials.getToken();

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

		if (res.equals("SUCCESS")) {

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String title = "Purchase Confirmation";
			out.println("<Html><Head><Title>" + title + "</Title></Head>\n<Body Bgcolor=\"#FDF5E6\">\n<H1 Align=Center>" + title + "</H1>\n" +
					"<p>  Thank you for your payment. Your transaction has been completed, and a receipt for your purchase has been emailed to you. "
					+ "  You may log into your account at www.sandbox.paypal.com/us to view details of this transaction."

					+ "<Table Border=1 Align=Center>\n" + "<Tr Bgcolor=\"#FFAD00\"><Th>Description</Th><Th>Value</Th></Tr>");


			en = request.getParameterNames();
			while (en.hasMoreElements()) {
				String paramName = (String) en.nextElement();
				String paramValue = request.getParameter(paramName);
				out.println("<tr><td>" + paramName + "</td><td>" + URLDecoder.decode(paramValue, "UTF-8") + "</td></tr>");

			}
		}
	}

}
