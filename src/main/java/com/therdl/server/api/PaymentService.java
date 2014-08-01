package com.therdl.server.api;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.server.data.PaypalCredentials;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Payment processing class. At the time of writing we use the PAYPAL merchant SDK to accept subscriptions
 * Created by Alex on 12/02/14.
 */
public interface PaymentService {


	/**
	 * Process the payment subscription with PayPal and return the user with the title RDLSupporter if successful
	 *
	 * @param currentUserBean a completed bean with paypalId present
	 * @return user bean with title if successful
	 */
	public AutoBean<CurrentUserBean> processRdlSupporter(AutoBean<CurrentUserBean> currentUserBean);

	/**
	 * Retrieve the paypal credentials from the database
	 *
	 * @param type
	 * @return
	 */
	public PaypalCredentials getPaypalCredentials(String type);
}
