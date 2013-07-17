package com.therdl.server.restapi;


import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Singleton
public class SnipDispatcherServlet extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(SnipDispatcherServlet.class);
    private final Provider<HttpSession> sessions;
    SnipsService snipsService;

    @Inject
    public SnipDispatcherServlet(Provider<HttpSession> sessions, SnipsService snipsService) {
        this.sessions = sessions;
        this.snipsService = snipsService;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {
        resp.setContentType("application/json");

        String debugString = snipsService.getDebugString();
        sLogger.info("SnipDispatcherServlet:  "+debugString );


        List<SnipBean> beans = snipsService.getAllSnips("demoUser id");
        sLogger.info("SnipDispatcherServlet: beans.size() "+beans.size());
        ArrayList<HashMap<String,String>> beanList = new ArrayList<HashMap<String,String>>();
        int k = 0;
        for (SnipBean bean : beans )   {
            HashMap<String,String> 	beanBag = new HashMap<String, String>();
            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            String asJson = AutoBeanCodex.encode(autoBean).getPayload();
            beanBag.put(Integer.toString(k),asJson);
            beanList.add(beanBag);
            k++;


        }

        sLogger.info("SnipDispatcherServlet: beanList.size() "+beanList.size());

        Gson gson = new Gson();
        sLogger.info(gson.toJson(beanList));
        PrintWriter out = resp.getWriter();
        out.write(gson.toJson(beanList));

    }


}

