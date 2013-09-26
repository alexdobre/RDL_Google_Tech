package com.therdl.server.restapi;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
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
 * Session controller for simple user authentication
 *
 */

@Singleton
public class SessionServlet  extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(SessionServlet.class);

    private final Provider<HttpSession> session;
    private Beanery beanery;
    UserService userService;

    @Inject
    public SessionServlet(Provider<HttpSession> sessions , UserService userService) {
        this.session = sessions;
        this.userService = userService;
        beanery = AutoBeanFactorySource.create(Beanery.class);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {

        resp.setContentType("application/json");
        // get the json
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        br.close();

        System.out.println( "SessionServlet signUp authBean json recieved" +sb.toString());

        AutoBean<AuthUserBean> authBean = AutoBeanCodex.decode(beanery, AuthUserBean.class, sb.toString());

        String action =  authBean.as().getAction();
        System.out.println( "SessionServlet signUp authBean.as().getAction() " +authBean.as().getAction());


        if(action.equals("signUp")) {

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
            session.get().setAttribute("userid",newUserBean.as().getEmail() );
            System.out.println("SessionServlet signUp authBean" + AutoBeanCodex.encode(authBean).getPayload());
            PrintWriter out = resp.getWriter();
            out.write( AutoBeanCodex.encode(authBean).getPayload());

        } // end sign up


        else if(action.equals("auth") )  {

            String password = authBean.as().getPassword();
            // get the user from the database if exists
            AutoBean<AuthUserBean> checkedUser = userService.findUser(authBean.as(), password);

            if(checkedUser.as().getAction().equals("OkUser")) {
            String avatarUrl = "userAvatar"+ File.separator+ checkedUser.as().getName()+"small.jpg";
            checkedUser.as().setAvatarUrl(avatarUrl);
            checkedUser.as().setAuth(true);
            // we can use this server side to obtain userId from session
            session.get().setAttribute("userid",checkedUser.as().getEmail() );

            } else {
            checkedUser.as().setAuth(false);
            }

            System.out.println(AutoBeanCodex.encode(checkedUser).getPayload());
            PrintWriter out = resp.getWriter();
            out.write( AutoBeanCodex.encode(checkedUser).getPayload());


        }

    } // end doPost

}