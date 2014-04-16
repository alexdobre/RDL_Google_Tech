package com.therdl.server.paypal_payment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.paypal.ipn.IPNMessage;
import com.therdl.server.api.UserService;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.UserBean;
import urn.ebay.api.PayPalAPI.GetRecurringPaymentsProfileDetailsResponseType;

import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class PayPalIPNServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private GetRecurringPaymentsProfileDetails getRecurringPaymentsProfileDetails = new GetRecurringPaymentsProfileDetails();
    private UserService userService;
    private Provider<HttpSession> session;
    private Beanery beanery = AutoBeanFactorySource.create(Beanery.class);

    Logger log = Logger.getLogger(this.getClass().toString());

    @Inject
    public PayPalIPNServlet(Provider<HttpSession> session, UserService userService) {
        this.session = session;
        this.userService = userService;

    }

    /*
     * receiver for PayPal ipn call back.
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        log.info("PayPalIPNServlet - START!");


        // For a full list of configuration parameters refer in wiki page.
        // (https://github.com/paypal/sdk-core-java/wiki/SDK-PayPalConfiguration-Parameters)
        Map<String, String> configurationMap = PayPalConfiguration.getConfig();
        IPNMessage ipnlistener = new IPNMessage(request, configurationMap);
        boolean isIpnVerified = ipnlistener.validate();
        String transactionType = ipnlistener.getTransactionType();
        Map<String, String> map = ipnlistener.getIpnMap();

        log.info("******* IPN (name:value) pair : " + map + "  " +
                "######### TransactionType : " + transactionType + "  ======== IPN verified : " + isIpnVerified);

        if (!transactionType.equals(PayPalIPNVariables.EXPRESS_CHECKOUT)) {
            //The most important part of this request is the recurring_payment_id.
            // This field is actually equivalent to a PayPal Express Checkout Recurring Payment Profile ID
            // and can be passed as the Profile ID of a GetRecurringPaymentsProfileDetails SOAP API operation.
            String profileId = map.get(PayPalIPNVariables.RECURRING_PAYMENT_ID);
            log.info("RECURRING_PAYMENT_ID: " + profileId);

            String timeCreated = map.get(PayPalIPNVariables.TIME_CREATED);
            log.info("TIME_CREATED: " + timeCreated);

            String nextPaymentDate = map.get(PayPalIPNVariables.NEXT_PAYMENT_DATE);
            log.info("NEXT_PAYMENT_DATE: " + nextPaymentDate);


            if (transactionType.equals(PayPalIPNVariables.RECURRING_PAYMENT)) {
                processRecurringPayment(profileId, nextPaymentDate);

            } else if (transactionType.equals(PayPalIPNVariables.RECURRING_PAYMENT_PROFILE_CREATED)) {
                processRecurringPaymentProfileCreated(profileId, timeCreated, nextPaymentDate);

            } else if (transactionType.equals(PayPalIPNVariables.RECURRING_PAYMENT_PROFILE_CANCEL)
                    || transactionType.equals(PayPalIPNVariables.RECURRING_PAYMENT_SUSPENDED)
                    || transactionType.equals(PayPalIPNVariables.RECURRING_PAYMENT_EXPIRED)
                    || transactionType.equals(PayPalIPNVariables.RECURRING_PAYMENT_SUSPENDED_DUE_TO_MAX_FAILED_PAYMENT)) {
                processRecurringPaymentProfileCancel(profileId);

            }

        }

    }

    private void logRecurringTransactions() {

    }

    private void processRecurringPaymentProfileCancel(String profileId) {
        UserBean userBean = userService.getUserByPayPalId(profileId);

        //remove the title/s of the canceled/suspended/expired subscriber
        List<UserBean.TitleBean> titleBeans = userBean.getTitles();
        titleBeans.clear();
        userService.updateUser(userBean);
    }

    private void processRecurringPaymentProfileCreated(String profileId, String timeCreated, String nextPaymentDate) {

        UserBean userBean = userService.getUserByPayPalId(profileId);

        List<UserBean.TitleBean> titleBeans = new ArrayList<>();
        UserBean.TitleBean titleBean = beanery.userTitleBean().as();
        titleBean.setTitleName(RDLConstants.UserTitle.RDL_SUPPORTER);
        titleBean.setDateGained(timeCreated);
        titleBean.setExpires(nextPaymentDate);
        titleBeans.add(titleBean);

        userBean.setTitles(titleBeans);
        userService.updateUser(userBean);
    }

    private void processRecurringPayment(String profileId, String nextPaymentDate) {

        UserBean userBean = userService.getUserByPayPalId(profileId);

        log.info("Expiration : " + nextPaymentDate);

        List<UserBean.TitleBean> titleBeans = userBean.getTitles();
        for (UserBean.TitleBean titleBean : titleBeans) {
            titleBean.setExpires(nextPaymentDate);
        }
        userService.updateUser(userBean);
    }
}
