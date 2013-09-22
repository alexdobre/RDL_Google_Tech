package com.therdl.server.restapi;


import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.UserBean;
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
public class UserDispatcherServlet extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(UserDispatcherServlet.class);
    private final Provider<HttpSession> sessions;

    /**
     * the service , which handles the db queries
     */
    UserService userService;

    /**
     * for message beans
     */
    Beanery beanery;

    /**
     * Guice injector
     * @param sessions
     * @param userService
     */
    @Inject
    public UserDispatcherServlet(Provider<HttpSession> sessions, UserService userService) {
        this.sessions = sessions;
        this.userService = userService;
        beanery = AutoBeanFactorySource.create(Beanery.class);
    }

    /**
     * Maven : will be executed on POST request with 'http://localhost:8080/rdl/getUsers' URL
     * Jboss : will be executed on POST request with 'http://localhost:8080/therdl/rdl/getUsers' URL
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        sLogger.info("UserDispatcherServlet: doPost : " + req);


//        PrintWriter out = resp.getWriter();
//        out.write("done");


        String debugString = userService.getDebugString();
        sLogger.info("UserDispatcherServlet:  "+debugString );

        // get the json
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        br.close();

       // this is a user bean refactor !!
        AutoBean<UserBean> actionBean = AutoBeanCodex.decode(beanery, UserBean.class, sb.toString());
        sb.setLength(0);

        if(actionBean.as().getAction().equals("getall") ) {
            List< UserBean > beans = userService.getAllUsers();
            sLogger.info("UserDispatcherServlet: beans.size() "+beans.size());
            sLogger.info("UserDispatcherServlet: actionBean.as().getAction() getall "+actionBean.as().getAction());
            ArrayList<HashMap<String,String>> beanList = new ArrayList<HashMap<String,String>>();
            int k = 0;
            for (UserBean bean : beans )   {
                HashMap<String,String> 	beanBag = new HashMap<String, String>();
                AutoBean<UserBean> autoBean = AutoBeanUtils.getAutoBean(bean);
                String asJson = AutoBeanCodex.encode(autoBean).getPayload();
                beanBag.put(Integer.toString(k),asJson);
                beanList.add(beanBag);
                k++;
            }

            sLogger.info("UserDispatcherServlet: beanList.size() "+beanList.size());

            Gson gson = new Gson();
            sLogger.info(gson.toJson(beanList));
            PrintWriter out = resp.getWriter();
            out.write(gson.toJson(beanList));
            beanList.clear();
            actionBean.as().setAction("dump");
        }

        else if(actionBean.as().getAction().equals("save") ) {
            sLogger.info("UserDispatcherServlet: actionBean.as().getAction() save "+actionBean.as().getAction());
            // action bean is actually a bean to be submitted for saving
            sLogger.info("UserDispatcherServlet:submitted bean for saving recieved  "+actionBean.as().getEmail());
            userService.createUser(actionBean.as());
        }
        else if(actionBean.as().getAction().equals("update") ) {
            sLogger.info("UserDispatcherServlet: actionBean.as().getAction() update "+actionBean.as().getAction());
            sLogger.info("UserDispatcherServlet:submitted bean for update recieved  "+actionBean.as().getEmail());
            userService.updateUser(actionBean.as());
        }
        else if(actionBean.as().getAction().equals("delete") ) {
            sLogger.info("UserDispatcherServlet: actionBean.as().getAction() delete "+actionBean.as().getAction());
            sLogger.info("UserDispatcherServlet:submitted bean for update recieved  "+actionBean.as().getId());
            userService.deleteUser(actionBean.as().getId());
        }
    }

}

