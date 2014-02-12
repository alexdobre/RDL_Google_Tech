package com.therdl.server.restapi;


import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;

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
import java.util.logging.Logger;

/**
 * SnipDispatcherServlet controller. This project uses the Guice injection
 * schema for beans, see http://code.google.com/p/google-guice/wiki/SpringComparison
 * if you are from the Spring framework space
 *
 * SnipDispatcherServlet uses Guice to implement  the command pattern re Gang of 4 design patterns
 * see http://java.dzone.com/articles/design-patterns-command
 *
 * @ HttpSession sessions, Servlet 3 api session object, use this for the current user id
 * @ SnipsService snipsService  see com.therdl.server.apiimpl.SnipServiceImpl java doc
 * @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * for new developers important to understand GWT AutoBean client/server architecture
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanFactory
 *
 */
@Singleton
public class SnipDispatcherServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(SnipDispatcherServlet.class.getName());
    private final Provider<HttpSession> sessions;

    /**
     * the service , which handles the db queries
     */
    SnipsService snipsService;

    UserService userService;

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
    public SnipDispatcherServlet(Provider<HttpSession> sessions, SnipsService snipsService, UserService userService) {
        this.sessions = sessions;
        this.snipsService = snipsService;
        this.userService = userService;
        beanery = AutoBeanFactorySource.create(Beanery.class);

    }

    /**
     * When code is running in the Maven Jetty plugin (development) the uri for this method will be
     * 'http://localhost:8080/rdl/getSnips' URL
     *
     * When code is running in the JBoss Application server (deployment) the uri for this method will be
     * 'http://localhost:8080/therdl/rdl/getSnips' URL
     * @param req HttpServletRequest Standard Http ServletRequest
     * @param resp HttpServletResponse Standard Http ServletResponse
     * @throws ServletException
     * @throws IOException
     *
     * String debugString, for testing injection scheme is wired up OK
     * AutoBean<SnipBean> actionBean see this video for a great explanation of 'actions' in the command pattern
     * http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
     * here the actionBean relates the users requested action
     * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex for serverside
     * autobean serialisation
     *
     * Gson gson see http://code.google.com/p/google-gson/ for Gson serialaisation
     *
     *
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String debugString = snipsService.getDebugString();
        log.info("SnipDispatcherServlet:  "+debugString );

        // get the json
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        br.close();
        log.info("SnipDispatcherServlet: sb.toString()  "+sb.toString() );

        // action bean as not all fields may have been set
        AutoBean<SnipBean> actionBean = AutoBeanCodex.decode(beanery, SnipBean.class, sb.toString());
        sb.setLength(0);

        if(actionBean.as().getAction().equals("getall")) {
            List<SnipBean> beans = snipsService.getAllSnips(actionBean.as().getPageIndex());
            log.info("SnipDispatcherServlet: beans.size() "+beans.size());
            log.info("SnipDispatcherServlet: actionBean.as().getAction() getall "+actionBean.as().getAction());

            ArrayList<HashMap<String,String>> beanList = getBeanList(beans);

            log.info("SnipDispatcherServlet: beanList.size() "+beanList.size());

            Gson gson = new Gson();
            log.info(gson.toJson(beanList));
            PrintWriter out = resp.getWriter();
            out.write(gson.toJson(beanList));
            beanList.clear();
            actionBean.as().setAction("dump");
        }

        else if(actionBean.as().getAction().equals("search")) {
            List <SnipBean> beans = snipsService.searchSnipsWith(actionBean.as(), actionBean.as().getPageIndex());
            log.info("SnipDispatcherServlet: beans.size() "+beans.size());
            log.info("SnipDispatcherServlet: actionBean.as().getAction() getall "+actionBean.as().getAction());

            ArrayList<HashMap<String,String>> beanList = getBeanList(beans);

            log.info("SnipDispatcherServlet: beanList.size() "+beanList.size());

            Gson gson = new Gson();
            log.info(gson.toJson(beanList));
            PrintWriter out = resp.getWriter();
            out.write(gson.toJson(beanList));
            beanList.clear();
            actionBean.as().setAction("dump");
        }
        else if(actionBean.as().getAction().equals("getSnip")) {
            SnipBean bean = snipsService.getSnip(actionBean.as().getId());
            log.info("SnipDispatcherServlet: actionBean.id "+actionBean.as().getId());
            log.info("SnipDispatcherServlet: bean.id "+bean.getId());
            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(autoBean).getPayload());
        }
        else if(actionBean.as().getAction().equals("viewSnip")) {
            SnipBean bean = snipsService.incrementCounter(actionBean.as().getId(), RDLConstants.SnipFields.VIEWS);
            log.info("SnipDispatcherServlet: actionBean.id "+actionBean.as().getId());
            log.info("SnipDispatcherServlet: bean.id "+bean.getId());

            String viewerId = actionBean.as().getViewerId();
            if(viewerId != null) {
                bean.setIsRepGivenByUser(userService.isRepGivenForSnip(viewerId, actionBean.as().getId()));
                bean.setIsRefGivenByUser(userService.isRefGivenForSnip(viewerId, actionBean.as().getId()));
            } else {
                // none logged users can't give a reputation and leave a reference
                bean.setIsRepGivenByUser(1);
                bean.setIsRefGivenByUser(1);
            }

            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(autoBean).getPayload());
        }
        else if(actionBean.as().getAction().equals("save") ) {
            log.info("SnipDispatcherServlet: actionBean.as().getAction() save "+actionBean.as().getAction());
            // action bean is actually a bean to be submitted for saving
            log.info("SnipDispatcherServlet:submitted bean for saving recieved  "+actionBean.as().getTitle());
            // check that user id has been set for this bean
            String id = (String) sessions.get().getAttribute("userid");
            String userAvatarUrl =   "userAvatar/"+id+"small.jpg";
            actionBean.as().setAvatarUrl(userAvatarUrl);
            actionBean.as().setCreationDate(snipsService.makeTimeStamp());
            snipsService.createSnip(actionBean.as());
        }
        else if(actionBean.as().getAction().equals("update") ) {
            log.info("SnipDispatcherServlet: actionBean.as().getAction() update "+actionBean.as().getAction());
            log.info("SnipDispatcherServlet:submitted bean for update recieved  "+actionBean.as().getTitle());
            actionBean.as().setEditDate(snipsService.makeTimeStamp());
            snipsService.updateSnip(actionBean.as());
        }
        else if(actionBean.as().getAction().equals("delete") ) {
            log.info("SnipDispatcherServlet: actionBean.as().getAction() delete "+actionBean.as().getAction());
            log.info("SnipDispatcherServlet:submitted bean for update recieved  "+actionBean.as().getId());
            snipsService.deleteSnip(actionBean.as().getId());
        }
        else if(actionBean.as().getAction().equals("saveReference")) {
            log.info("SnipDispatcherServlet: saveReference");

            // increments reference counter of parent snip for reference type (positive/neutral/negative)
            String parentSnipId = actionBean.as().getId();

            String counterField = RDLConstants.SnipFields.POS_REF;
            if(actionBean.as().getReferenceType() != null) {
                if(actionBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEUTRAL))
                    counterField = RDLConstants.SnipFields.NEUTRAL_REF;
                else if(actionBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEGATIVE))
                    counterField = RDLConstants.SnipFields.NEGATIVE_REF;

            } else if(actionBean.as().getSnipType().equals(RDLConstants.SnipType.PLEDGE)) {
                counterField = RDLConstants.SnipFields.PLEDGES;
            } else if (actionBean.as().getSnipType().equals(RDLConstants.SnipType.COUNTER)) {
                counterField = RDLConstants.SnipFields.COUNTERS;
            } else if (actionBean.as().getSnipType().equals(RDLConstants.SnipType.POST)) {
                counterField = RDLConstants.SnipFields.POSTS;
            }

            snipsService.incrementCounter(parentSnipId, counterField);

            // reset snip id and call create snip to save reference object as separate entity in the snip collection
            actionBean.as().setId(null);
            actionBean.as().setCreationDate(snipsService.makeTimeStamp());
            String referenceId = snipsService.createSnip(actionBean.as());

            // add reference as Link object into the parent snip
            AutoBean<SnipBean.Link> linkAutoBean = beanery.snipLinksBean();
            linkAutoBean.as().setRank("0");
            linkAutoBean.as().setTargetId(referenceId);
            SnipBean bean = snipsService.addReference(linkAutoBean, parentSnipId);

            // in the case of post a user can reply the save thread more than one time, so no need to save this information
            if(!actionBean.as().getSnipType().equals(RDLConstants.SnipType.POST)) {
                String email = (String) sessions.get().getAttribute("userid");
                AutoBean<UserBean.RefGivenBean> refGivenBean = beanery.userRefGivenBean();
                refGivenBean.as().setSnipId(parentSnipId);
                refGivenBean.as().setDate(snipsService.makeTimeStamp());
                userService.addRefGiven(refGivenBean, email);
            }
            // send modified parent snip as json
            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(autoBean).getPayload());
        }
        else if(actionBean.as().getAction().equals("getReferences")) {
            List<SnipBean> beanReferences = snipsService.getReferences(actionBean.as(), actionBean.as().getPageIndex());
            String viewerId = actionBean.as().getViewerId();
            if(viewerId != null) {
                userService.setRepGivenForSnips(viewerId, beanReferences);
            }

            ArrayList<HashMap<String,String>> beanList = getBeanList(beanReferences);

            Gson gson = new Gson();
            log.info(gson.toJson(beanList));
            PrintWriter out = resp.getWriter();
            out.write(gson.toJson(beanList));
            beanList.clear();
            actionBean.as().setAction("dump");
        }

        else if(actionBean.as().getAction().equals("giveRep")) {
            String email = (String) sessions.get().getAttribute("userid");

            AutoBean<UserBean.RepGivenBean> repGivenBean = beanery.userRepGivenBean();
            repGivenBean.as().setSnipId(actionBean.as().getId());
            repGivenBean.as().setDate(snipsService.makeTimeStamp());
            UserBean userBean = userService.addRepGiven(repGivenBean, email);

            SnipBean bean = snipsService.incrementCounter(actionBean.as().getId(), RDLConstants.SnipFields.REP);

            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(autoBean).getPayload());
        }
    }

    /**
     * creates list of beans for response
     * @param beans
     * @return
     */
    private ArrayList<HashMap<String,String>> getBeanList(List<SnipBean> beans) {
        ArrayList<HashMap<String,String>> beanList = new ArrayList<HashMap<String,String>>();
        int k = 0;
        for (SnipBean bean : beans) {
            HashMap<String,String> 	beanBag = new HashMap<String, String>();
            AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
            String asJson = AutoBeanCodex.encode(autoBean).getPayload();
            beanBag.put(Integer.toString(k),asJson);
            beanList.add(beanBag);
            k++;
        }

        return beanList;
    }

}

