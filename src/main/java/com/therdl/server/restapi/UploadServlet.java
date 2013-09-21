package com.therdl.server.restapi;

import com.google.inject.Singleton;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.UserService;
import com.therdl.shared.beans.Beanery;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 */

@Singleton
public class UploadServlet extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(UploadServlet.class);
    private final Provider<HttpSession> sessions;
    private Beanery beanery;

    @Inject
    public UploadServlet(Provider<HttpSession> sessions , UserService userService) {
        this.sessions = sessions;
        beanery = AutoBeanFactorySource.create(Beanery.class);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {

        System.out.println( "UploadServlet is go ");



    }


}
