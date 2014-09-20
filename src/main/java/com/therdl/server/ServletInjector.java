package com.therdl.server;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.therdl.server.api.RepService;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.server.apiimpl.RepServiceImpl;
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
import com.therdl.server.restapi.GcseServlet;
import com.therdl.server.restapi.SnipDispatcherServlet;
import com.therdl.server.validator.SnipValidator;
import com.therdl.server.validator.TokenValidator;
import com.therdl.server.validator.UserValidator;
import com.therdl.server.validator.impl.SnipValidatorImpl;
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
				bind(RepService.class).to(RepServiceImpl.class);
				bind(UserService.class).to(UserServiceImpl.class);
				bind(SnipValidator.class).to(SnipValidatorImpl.class);
				bind(TokenValidator.class).to(TokenValidatorImpl.class);
				bind(UserValidator.class).to(UserValidatorImpl.class);
				bind(SnipValidator.class).to(SnipValidatorImpl.class);

				filter("/*").through(CrawlFilter.class);

				serve("/rdl/getSnips").with(SnipDispatcherServlet.class);
				serve("/rdl/getSession").with(AuthServlet.class);
				serve("/rdl/avatarUpload").with(AmazonS3UploadServlet.class);
				serve("/rdl/gcse").with(GcseServlet.class);
				serve(PayPalConstants.PAYPAL_IPN_NOTIFY_URL).with(PayPalIPNServlet.class);
				serve(PayPalConstants.PAYPAL_CHECKOUT_URL).with(PaypalSubscriptionServlet.class);
				serve(PayPalConstants.PAYPAL_RETURN_URL).with(PaypalSubscriptionCallbackServlet.class);
			}
		});
	}
}
