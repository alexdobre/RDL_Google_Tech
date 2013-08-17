package com.therdl.server.restapi;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Session controller for simple user authentication
 *
 */

@Singleton
public class SessionServlet  extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(SessionServlet.class);

    private final Provider<HttpSession> sessions;
    private Beanery beanery;


    @Inject
    public SessionServlet(Provider<HttpSession> sessions) {
        this.sessions = sessions;
        beanery = AutoBeanFactorySource.create(Beanery.class);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {

      //  sessions.get().setAttribute(HoloConstants.TOS_ACCEPTED_KEY, authUser.getTosaccepted());
        resp.setContentType("application/json");
        // get the json
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        br.close();

        AutoBean<AuthUserBean> authBean = AutoBeanCodex.decode(beanery, AuthUserBean.class, sb.toString());
        authBean.as().setAuth(true);
        AutoBeanCodex.encode(authBean).getPayload();

        sLogger.info( AutoBeanCodex.encode(authBean).getPayload());
        PrintWriter out = resp.getWriter();
        out.write( AutoBeanCodex.encode(authBean).getPayload());


    }

}
