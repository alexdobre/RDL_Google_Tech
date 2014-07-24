package com.therdl.server.apiimpl;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.mongodb.*;
import com.therdl.server.api.PaymentService;
import com.therdl.server.data.DbProvider;
import com.therdl.server.data.PaypalCredentials;
import com.therdl.server.util.ServerUtils;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

/**
 * Implementation of payment processing. At the time of writing we use the Paypal merchant SDK to process user subscriptions
 * Created by Alex on 12/02/14.
 */
@Singleton
public class PaymentServiceImpl implements PaymentService {

	private static Logger log = Logger.getLogger("");

    private Beanery beanery;
	private DbProvider dbProvider;

	@Inject
	public PaymentServiceImpl (DbProvider dbProvider){
		this.dbProvider = dbProvider;
	}

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

    @Override
    public PaypalCredentials getPaypalCredentials(String type) {
        //check for null
        if (type == null || (type != "pdt" && type != "ipn")) {
            log.severe("getPaypalCredentials wrong type supplied: " + type);
            return null;
        }

        DB db = dbProvider.getDb();
        //get the mail credentials from the DB
        DBCollection coll = db.getCollection("paypalCredentials");

        BasicDBObject query = new BasicDBObject();
        query.put("uid", type);

        DBCursor cursor = coll.find(query);
        DBObject doc = cursor.next();

        PaypalCredentials cred = new PaypalCredentials();
        cred.setType((String) doc.get("type"));
        cred.setUrl((String) doc.get("url"));
        if (doc.containsField("token")) {
            cred.setToken((String) doc.get("token"));
        }

        return cred;
    }
}
