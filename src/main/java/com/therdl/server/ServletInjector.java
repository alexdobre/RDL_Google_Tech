package com.therdl.server;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.therdl.server.api.SnipsService;
import com.therdl.server.apiimpl.SnipServiceImpl;
import com.therdl.server.restapi.SessionServlet;
import com.therdl.server.restapi.SnipDispatcherServlet;
import org.slf4j.LoggerFactory;


public class ServletInjector extends GuiceServletContextListener {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(ServletInjector.class);



    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {


            @Override
            protected void configureServlets() {

                bind(SnipsService.class).to(SnipServiceImpl.class);

                serve("/rdl/getSnips").with(SnipDispatcherServlet.class);

                serve("/rdl/getSession").with(SessionServlet.class);
            }

        }); }






}
