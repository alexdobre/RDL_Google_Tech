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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Singleton
public class SnipDispatcherServlet extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(SnipDispatcherServlet.class);
    private final Provider<HttpSession> sessions;

    /**
     * the service , which handles the db queries
     */
    SnipsService snipsService;

    /**
     * for message beans
     */
    Beanery beanery;


    /**
     * Guice injector
     * @param sessions
     * @param snipsService
     */
    @Inject
    public SnipDispatcherServlet(Provider<HttpSession> sessions, SnipsService snipsService) {
        this.sessions = sessions;
        this.snipsService = snipsService;
        beanery = AutoBeanFactorySource.create(Beanery.class);

    }

    /**
     * Maven : will be executed on POST request with 'http://localhost:8080/rdl/getSnips' URL
     * Jboss : will be executed on POST request with 'http://localhost:8080/therdl/rdl/getSnips' URL
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String debugString = snipsService.getDebugString();
        sLogger.info("SnipDispatcherServlet:  "+debugString );

        // get the json
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        br.close();
        sLogger.info("SnipDispatcherServlet: sb.toString()  "+sb.toString() );

        // action bean as not all fields may have been set
        AutoBean<SnipBean> actionBean = AutoBeanCodex.decode(beanery, SnipBean.class, sb.toString());
        sb.setLength(0);

        if(actionBean.as().getAction().equals("getall") ) {
            List < SnipBean > beans = snipsService.getAllSnips();
            sLogger.info("SnipDispatcherServlet: beans.size() "+beans.size());
            sLogger.info("SnipDispatcherServlet: actionBean.as().getAction() getall "+actionBean.as().getAction());
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
            beanList.clear();
            actionBean.as().setAction("dump");
        }

        else if(actionBean.as().getAction().equals("search")) {
            List < SnipBean > beans = snipsService.searchSnipsWith(actionBean.as());
            sLogger.info("SnipDispatcherServlet: beans.size() "+beans.size());
            sLogger.info("SnipDispatcherServlet: actionBean.as().getAction() getall "+actionBean.as().getAction());
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
            beanList.clear();
            actionBean.as().setAction("dump");
        }
        else if(actionBean.as().getAction().equals("getSnip")) {
            SnipBean bean = snipsService.getSnip(actionBean.as().getId());
            sLogger.info("SnipDispatcherServlet: actionBean.id "+actionBean.as().getId());
            sLogger.info("SnipDispatcherServlet: bean.id "+bean.getId());
            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(autoBean).getPayload());
        }
        else if(actionBean.as().getAction().equals("viewSnip")) {
            SnipBean bean = snipsService.incrementViewCounter(actionBean.as().getId());
            sLogger.info("SnipDispatcherServlet: actionBean.id "+actionBean.as().getId());
            sLogger.info("SnipDispatcherServlet: bean.id "+bean.getId());
            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(autoBean).getPayload());
        }
        else if(actionBean.as().getAction().equals("save") ) {
            sLogger.info("SnipDispatcherServlet: actionBean.as().getAction() save "+actionBean.as().getAction());
            // action bean is actually a bean to be submitted for saving
            sLogger.info("SnipDispatcherServlet:submitted bean for saving recieved  "+actionBean.as().getTitle());
            // check that user id has been set for this bean
            String id = (String) sessions.get().getAttribute("userid");
            String userAvatarUrl =   "userAvatar/"+id+"small.jpg";
            actionBean.as().setAvatarUrl(userAvatarUrl);
            actionBean.as().setCreationDate(snipsService.makeTimeStamp());
            snipsService.createSnip(actionBean.as());
        }
        else if(actionBean.as().getAction().equals("update") ) {
            sLogger.info("SnipDispatcherServlet: actionBean.as().getAction() update "+actionBean.as().getAction());
            sLogger.info("SnipDispatcherServlet:submitted bean for update recieved  "+actionBean.as().getTitle());
            actionBean.as().setEditDate(snipsService.makeTimeStamp());
            snipsService.updateSnip(actionBean.as());
        }
        else if(actionBean.as().getAction().equals("delete") ) {
            sLogger.info("SnipDispatcherServlet: actionBean.as().getAction() delete "+actionBean.as().getAction());
            sLogger.info("SnipDispatcherServlet:submitted bean for update recieved  "+actionBean.as().getId());
            snipsService.deleteSnip(actionBean.as().getId());
        }
    }


}

