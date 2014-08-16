package com.therdl.server;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.therdl.server.api.PaymentService;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.server.apiimpl.PaymentServiceImpl;
import com.therdl.server.apiimpl.SnipServiceImpl;
import com.therdl.server.apiimpl.UserServiceImpl;
import com.therdl.server.crawler.CrawlFilter;
import com.therdl.server.data.DbProvider;
import com.therdl.server.data.DbProviderImpl;
import com.therdl.server.paypal_payment.PayPalConstants;
import com.therdl.server.paypal_payment.PayPalIPNServlet;
import com.therdl.server.paypal_payment.PaypalSubscriptionCallbackServlet;
import com.therdl.server.paypal_payment.PaypalSubscriptionServlet;
import com.therdl.server.restapi.AmazonS3UploadServlet;
import com.therdl.server.restapi.AuthServlet;
import com.therdl.server.restapi.SnipDispatcherServlet;
import com.therdl.server.restapi.UserDispatcherServlet;
import com.therdl.server.validator.SnipsValidator;
import com.therdl.server.validator.TokenValidator;
import com.therdl.server.validator.UserValidator;
import com.therdl.server.validator.impl.SnipsValidatorImpl;
import com.therdl.server.validator.impl.TokenValidatorImpl;
import com.therdl.server.validator.impl.UserValidatorImpl;

/**
 * ServletInjector controller Guice injection. This project uses the Guice injection
 * schema for beans, see http://code.google.com/p/google-guice/wiki/SpringComparison
 * if you are from the Spring framework space
 * 2 layers Service and Servlet
 * 1. Service layer Schema  <type> is bound to <object>  explicitly
 * SnipsService SnipServiceImpl
 * UserService UserServiceImpl
 * DbFileServiceDbFileServiceImpl
 * FileStorage MongoFileStorage
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
				bind(PaymentService.class).to(PaymentServiceImpl.class);
				bind(SnipsValidator.class).to(SnipsValidatorImpl.class);
				bind(TokenValidator.class).to(TokenValidatorImpl.class);
				bind(UserValidator.class).to(UserValidatorImpl.class);

				filter("/*").through(CrawlFilter.class);

				serve("/rdl/getSnips").with(SnipDispatcherServlet.class);
				serve("/rdl/getUsers").with(UserDispatcherServlet.class);
				serve("/rdl/getSession").with(AuthServlet.class);
				serve("/rdl/avatarUpload").with(AmazonS3UploadServlet.class);
				serve(PayPalConstants.PAYPAL_IPN_NOTIFY_URL).with(PayPalIPNServlet.class);
				serve(PayPalConstants.PAYPAL_CHECKOUT_URL).with(PaypalSubscriptionServlet.class);
				serve(PayPalConstants.PAYPAL_RETURN_URL).with(PaypalSubscriptionCallbackServlet.class);
			}
		});
	}
}
