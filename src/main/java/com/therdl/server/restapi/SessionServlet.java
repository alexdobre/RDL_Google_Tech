package com.therdl.server.restapi;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.server.data.FileStorage;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Session controller for simple user authentication. This project uses the Guice injection
 * schema for beans, see http://code.google.com/p/google-guice/wiki/SpringComparison
 * if you are from the Spring framework space
 * <p/>
 * This class handles user authentication and session runtime data
 *
 * @ HttpSession sessions, Servlet 3 api session object, use this for the current user id
 * @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ UserService userService see com.therdl.server.apiimpl.UserServiceImpl java doc
 * @ FileStorage mongoFileStorage  see com.therdl.server.data.MongoFileStorage java doc
 * @ doPost the post method for this Servlet
 */

@Singleton
public class SessionServlet extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(SessionServlet.class);

    private final Provider<HttpSession> session;
    private Beanery beanery;
    private UserService userService;
    private FileStorage mongoFileStorage;

    @Inject
    public SessionServlet(Provider<HttpSession> sessions, UserService userService, FileStorage mongoFileStorage) {
        this.session = sessions;
        this.mongoFileStorage = mongoFileStorage;
        this.userService = userService;
        beanery = AutoBeanFactorySource.create(Beanery.class);


    }

    /**
     * This is the equivalent main method for this class
     *
     * @param HttpServletRequest  req  Standard Http ServletRequest
     * @param HttpServletResponse resp  Standard Http ServletResponse
     * @throws ServletException
     * @throws IOException      String contextRoot, obtains the top level directory to find resource in server runtime directory
     *                          String avatarDirUrl uri for avatar image constructed from contextRoot
     *                          AutoBean<AuthUserBean> authBean user credentials from client
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        // we need the path to the avatar file wherever it is deployed by jboss or wherever the application is running
        String contextRoot = getServletContext().getRealPath("/");
        String avatarDirUrl = contextRoot + File.separator + "userAvatar";

        // get the json
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        br.close();

        System.out.println("SessionServlet signUp authBean json recieved" + sb.toString());

        AutoBean<AuthUserBean> authBean = AutoBeanCodex.decode(beanery, AuthUserBean.class, sb.toString());

        String action = authBean.as().getAction();
        System.out.println("SessionServlet signUp authBean.as().getAction() " + authBean.as().getAction());


        if (action.equals("signUp")) {

            AutoBean<UserBean> newUserBean = beanery.userBean();
            System.out.println("SessionServlet password hash = " + authBean.as().getEmail());
            newUserBean.as().setEmail(authBean.as().getEmail());
            System.out.println("SessionServlet password hash = " + authBean.as().getName());
            newUserBean.as().setUsername(authBean.as().getName());
            String password = authBean.as().getPassword();
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println("SessionServlet password hash = " + hash);
            newUserBean.as().setPassHash(hash);
            userService.createUser(newUserBean.as());
            authBean.as().setAuth(true);
            authBean.as().setAction("newUserOk");
            authBean.as().setName(authBean.as().getName());
            String avatarUrl = avatarDirUrl + File.separator + "avatar-empty.jpg";
            authBean.as().setAvatarUrl(avatarUrl);
            session.get().setAttribute("userid", newUserBean.as().getEmail());
            session.get().setAttribute("username", newUserBean.as().getUsername());
            System.out.println("SessionServlet signUp authBean" + AutoBeanCodex.encode(authBean).getPayload());
            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(authBean).getPayload());

        } // end sign up


        else if (action.equals("auth")) {

            String password = authBean.as().getPassword();
            // get the user from the database if exists
            AutoBean<AuthUserBean> checkedUser = userService.findUser(authBean.as(), password);

            if (checkedUser.as().getAction().equals("OkUser")) {

                checkedUser.as().setAuth(true);
                // we can use this server side to obtain userId from session
                session.get().setAttribute("userid", checkedUser.as().getEmail());
                session.get().setAttribute("name", checkedUser.as().getName());
                // need to check if file exists and write to filesystem
                boolean avatarExists = mongoFileStorage.setAvatarForUserFromDb(avatarDirUrl, checkedUser.as().getName());

                if (avatarExists) {
                    // javascript from modulle base in target/war
                    String avatarUrl = "userAvatar" + File.separator + checkedUser.as().getName() + "small.jpg";
                    checkedUser.as().setAvatarUrl(avatarUrl);
                } else {
                    // javascript from modulle base in target/war
                    String avatarUrl = "userAvatar" + File.separator + "avatar-empty.jpg";
                    checkedUser.as().setAvatarUrl(avatarUrl);
                }
            } else {
                checkedUser.as().setAuth(false);
                // javascript from modulle base in target/war
                String avatarUrl = "userAvatar" + File.separator + "avatar-empty.jpg";
                checkedUser.as().setAvatarUrl(avatarUrl);
            }

            PrintWriter out = resp.getWriter();
            out.write(AutoBeanCodex.encode(checkedUser).getPayload());


        }

    } // end doPost

}