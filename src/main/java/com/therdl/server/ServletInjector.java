package com.therdl.server;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.therdl.server.api.DbFileService;
import com.therdl.server.api.PaymentService;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.server.apiimpl.DbFileServiceImpl;
import com.therdl.server.apiimpl.PaymentServiceImpl;
import com.therdl.server.apiimpl.SnipServiceImpl;
import com.therdl.server.apiimpl.UserServiceImpl;
import com.therdl.server.data.DbProvider;
import com.therdl.server.data.DbProviderImpl;
import com.therdl.server.data.FileStorage;
import com.therdl.server.data.MongoFileStorage;
import com.therdl.server.paypal_payment.PayPalConstants;
import com.therdl.server.paypal_payment.PayPalIPNServlet;
import com.therdl.server.paypal_payment.PaypalSubscriptionCallbackServlet;
import com.therdl.server.paypal_payment.PaypalSubscriptionServlet;
import com.therdl.server.restapi.SessionServlet;
import com.therdl.server.restapi.SnipDispatcherServlet;
import com.therdl.server.restapi.UploadServlet;
import com.therdl.server.restapi.UserDispatcherServlet;

/**
 * ServletInjector controller Guice injection. This project uses the Guice injection
 * schema for beans, see http://code.google.com/p/google-guice/wiki/SpringComparison
 * if you are from the Spring framework space
 * <p/>
 * 2 layers Service and Servlet
 * <p/>
 * 1. Service layer Schema  <type> is bound to <object>  explicitly
 * SnipsService SnipServiceImpl
 * UserService UserServiceImpl
 * DbFileServiceDbFileServiceImpl
 * FileStorage MongoFileStorage
 * <p/>
 * 2. Servlet layer
 * SnipDispatcherServlet controller for Snip operations
 * UserDispatcherServlet controller for User operations
 * SessionServlet controller for Session operations
 * UploadServlet controller for Upload operations
 */

public class ServletInjector extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {

			@Override
			protected void configureServlets() {
				bind(DbProvider.class).to(DbProviderImpl.class);
				bind(SnipsService.class).to(SnipServiceImpl.class);
				bind(UserService.class).to(UserServiceImpl.class);
				bind(DbFileService.class).to(DbFileServiceImpl.class);
				bind(FileStorage.class).to(MongoFileStorage.class);
				bind(PaymentService.class).to(PaymentServiceImpl.class);

				serve("/rdl/getSnips").with(SnipDispatcherServlet.class);
				serve("/rdl/getUsers").with(UserDispatcherServlet.class);
				serve("/rdl/getSession").with(SessionServlet.class);
				serve("/rdl/avatarUpload").with(UploadServlet.class);
				serve(PayPalConstants.PAYPAL_IPN_NOTIFY_URL).with(PayPalIPNServlet.class);
				serve(PayPalConstants.PAYPAL_CHECKOUT_URL).with(PaypalSubscriptionServlet.class);
				serve(PayPalConstants.PAYPAL_RETURN_URL).with(PaypalSubscriptionCallbackServlet.class);
			}
		});
	}
}
