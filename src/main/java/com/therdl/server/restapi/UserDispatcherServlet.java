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

    /*********************************** For demo CRUD ************************************/

    /**
     * Maven : the test (GET) URL : http://localhost:8080/rdl/getUsers
     * Jboss : the test (GET) URL : http://localhost:8080/therdl/rdl/getUsers
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        sLogger.info("UserDispatcherServlet: doGet : " + req);

        userService.dropUserCollection();

        createUser("Sofy");
        createUser("Nicole");

        // get all the users
        List<UserBean> users =  userService.getAllUsers();

        // create a StringBuilder to build the response
        StringBuilder buff = new StringBuilder() ;
        buff.append("Users are created and listed <br>");
        for (UserBean u :users) {
            buff.append("<br/> <h4>" + u.getUsername() + "</h4>");
            buff.append(u.toString());
        }

        // the id of the first user in the db
        String id = users.get(0).getId();

        // get the first user in the db
        UserBean user = userService.getUser(id);
        user.setRep("100000000");
        userService.updateUser(user);
        user = userService.getUser(id);

        buff.append("<br/><br/><hr><br/>Updated the user " + user.getUsername() +  " : The 'rep' field is changed<br>");
        buff.append(user.toString());
        buff.append("<br/><br/><br/><br/>");

        // delete the user
        userService.deleteUser(id);

        // retrieve al the user objects
        users =  userService.getAllUsers();

        buff.append("Delete the user " + user.getUsername() +  " <br/><br/>");
        buff.append("Listing all the users <br/>");
        for (UserBean u :users) {
            buff.append(u.toString());
            buff.append("<br/><br/>");
        }

        // display the buffered string
        PrintWriter out = resp.getWriter();
        out.write(buff.toString());
    }


    /**
     * creates the new user
     * @param username
     */
    private void createUser(String username) {

        UserBean user = beanery.userBean().as();

        user.setUsername(username);
        user.setPassHash("adksjsasdfofcfchaisud");
        user.setEmail("example@gmail.com");
        user.setRep("very high!!");
        user.setTitles(createTitles());
        user.setFriends(createFriends());
        user.setRepGiven(createRepGiven());
        user.setVotesGiven(createVotesGiven());

        userService.createUser(user);
    }


/************************************* sample object  ************************************/

    /**
     * Sample List<UserBean.VotesGivenBean> creation
     * @return
     */
    private List<UserBean.VotesGivenBean> createVotesGiven() {
        UserBean.VotesGivenBean votesGiven1 = beanery.userVotesGivenBean().as();
        UserBean.VotesGivenBean votesGiven2 = beanery.userVotesGivenBean().as();

        votesGiven1.setProposalId("1234");
        votesGiven1.setDate("23 aug 2012");

        votesGiven2.setProposalId("5678");
        votesGiven2.setDate("23 aug 2013");

        List<UserBean.VotesGivenBean> list = new ArrayList<UserBean.VotesGivenBean>();
        list.add(votesGiven1);
        list.add(votesGiven2);

        return list;
    }

    /**
     * Sample List<UserBean.RepGivenBean> creation
     * @return
     */
    private List<UserBean.RepGivenBean> createRepGiven() {
        UserBean.RepGivenBean repGiven1 = beanery.userRepGivenBean().as();
        UserBean.RepGivenBean repGiven2 = beanery.userRepGivenBean().as();

        repGiven1.setSnipId("323254");
        repGiven1.setDate("23 aug 2012");

        repGiven2.setSnipId("6546345");
        repGiven2.setDate("23 aug 2013");

        List<UserBean.RepGivenBean> list = new ArrayList<UserBean.RepGivenBean>();
        list.add(repGiven1);
        list.add(repGiven2);

        return list;
    }

    /**
     * Sample List<UserBean.FriendBean> creation
     * @return
     */
    private List<UserBean.FriendBean> createFriends() {
        UserBean.FriendBean friend1 = beanery.userFriendBean().as();
        UserBean.FriendBean friend2 = beanery.userFriendBean().as();

        friend1.setUsername("arsenA");
        friend1.setMessages(createMessages());

        friend2.setUsername("johnE");
        friend2.setMessages(createMessages());

        List<UserBean.FriendBean> list = new ArrayList<UserBean.FriendBean>();
        list.add(friend1);
        list.add(friend2);

        return list;
    }

    /**
     * sample List<UserBean.MessageBean> creation
     * @return
     */
    private List<UserBean.MessageBean> createMessages() {
        UserBean.MessageBean message1 = beanery.userMessageBean().as();
        UserBean.MessageBean message2 = beanery.userMessageBean().as();

        message1.setMessageId("245436");
        message1.setDate("23 aug 2013");

        message2.setMessageId("56748567");
        message2.setDate("23 aug 2013");

        List<UserBean.MessageBean> list = new ArrayList<UserBean.MessageBean>();
        list.add(message1);
        list.add(message2);

        return list;
    }

    /**
     * sample List<UserBean.TitleBean> creation
     * @return
     */
    private List<UserBean.TitleBean> createTitles() {
        UserBean.TitleBean title1 = beanery.userTitleBean().as();
        UserBean.TitleBean title2 = beanery.userTitleBean().as();

        title1.setTitleName("Title 1");
        title1.setDateGained("some data 1");

        title2.setTitleName("Title 2");
        title2.setDateGained("some data 2");

        List<UserBean.TitleBean> list = new ArrayList<UserBean.TitleBean>();
        list.add(title1);
        list.add(title2);

        return list;
    }
}

