package com.therdl.server.apiimpl;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.server.api.PaymentService;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;

import javax.inject.Singleton;
import java.util.logging.Logger;

/**
 * Implementation of payment processing. At the time of writing we use the Paypal merchant SDK to process user subscriptions
 * Created by Alex on 12/02/14.
 */
@Singleton
public class PaymentServiceImpl implements PaymentService{

    private Beanery beanery;
    private static Logger log = Logger.getLogger("");

    @Override
    public AutoBean<CurrentUserBean> processRdlSupporter(AutoBean<CurrentUserBean> currentUserBean) {

        //configure the paypal service with credentials from the database
//        Map<String, String> customConfigurationMap = new HashMap<String, String>();
//        customConfigurationMap.put("mode", "sandbox");
//        customConfigurationMap.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
//        customConfigurationMap.put("acct1.Password", "WX4WTU3S8MY44S7F");
//        customConfigurationMap.put("acct1.Signature", "AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
//        customConfigurationMap.put("acct1.AppId", "APP-80W284485P519543T");
//
//        PayPalAPIInterfaceServiceService paypalService = new PayPalAPIInterfaceServiceService(customConfigurationMap);
//
//        //create recurring payments profile
//        CreateRecurringPaymentsProfileReq  createRecurringPaymentsProfileReq = new CreateRecurringPaymentsProfileReq();
//        try {
//            paypalService.createRecurringPaymentsProfile(createRecurringPaymentsProfileReq);
//        } catch (SSLConfigurationException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (InvalidCredentialException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (IOException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (HttpErrorException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (InvalidResponseDataException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (ClientActionRequiredException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (MissingCredentialException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (InterruptedException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (OAuthException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (ParserConfigurationException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        } catch (SAXException e) {
//            log.log(Level.SEVERE,e.getMessage(),e);
//        }
        return null;
    }
}
