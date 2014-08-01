package com.therdl.server.restapi;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.therdl.server.paypal.IpnConfig;
import com.therdl.server.paypal.IpnException;
import com.therdl.server.paypal.IpnHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This servlet receives the Paypal Notifications and passes processing to the IpnHandler
 * Created by Alex on 2/1/14.
 */
@Singleton
public class IpnServletOld extends HttpServlet {

	private static Logger log = Logger.getLogger(IpnServletOld.class.getName());


	@Inject
	public IpnServletOld() {

	}

	/**
	 * When code is running in the Maven Jetty plugin (development) the uri for this method will be
	 * 'http://localhost:8080/rdl/ipn' URL
	 * <p/>
	 * When code is running in the JBoss Application server (deployment) the uri for this method will be
	 * 'http://localhost:8080/therdl/rdl/ipn' URL
	 *
	 * @param HttpServletRequest  req  Standard Http ServletRequest
	 * @param HttpServletResponse resp  Standard Http ServletResponse
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException            String userId users user id unique identifier
	 *                                        ServletFileUpload upload  apache commons file upload
	 *                                        String userName users user name
	 *                                        String avatarUrl relative uri to the image
	 *                                        AutoBean<AuthUserBean> actionBean see this video for a great explanation of 'actions' in the command pattern
	 *                                        http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
	 *                                        here the actionBean relates the users requested action
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			IpnHandler ipnHandler = new IpnHandler();
			// For Production/Live - https://www.paypal.com/cgi-bin/webscr
			// For Sandbox/Testing - https://www.sandbox.paypal.com/cgi-bin/webscr
			ipnHandler.setIpnConfig(new IpnConfig("https://www.sandbox.paypal.com/cgi-bin/webscr",
					"alx.dobre@gmail.com",
					"5",
					"EUR"));
			ipnHandler.handleIpn(req);
		} catch (IpnException e) {
			log.log(Level.SEVERE, "IPN exception", e);
		}
	}
}
