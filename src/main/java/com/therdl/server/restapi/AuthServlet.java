package com.therdl.server.restapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.UserService;
import com.therdl.server.data.DbProvider;
import com.therdl.server.util.EmailSender;
import com.therdl.server.util.ServerUtils;
import com.therdl.server.validator.UserValidator;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.InvalidInputException;
import com.therdl.shared.exceptions.RDLSendEmailException;
import com.therdl.shared.exceptions.RdlCodedException;
import com.therdl.shared.exceptions.TokenInvalidException;
import com.therdl.shared.exceptions.UserValidationException;

/**
 * Session controller for simple user authentication. This project uses the Guice injection
 * schema for beans, see http://code.google.com/p/google-guice/wiki/SpringComparison
 * if you are from the Spring framework space
 * This class handles user authentication and session runtime data
 *
 * @ HttpSession sessions, Servlet 3 api session object, use this for the current user id
 * @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ UserService userService see com.therdl.server.apiimpl.UserServiceImpl java doc
 * @ FileStorage mongoFileStorage  see com.therdl.server.data.MongoFileStorage java doc
 * @ doPost the post method for this Servlet
 */

@Singleton
public class AuthServlet extends HttpServlet {

	final Logger log = LoggerFactory.getLogger(AuthServlet.class);

	private final Provider<HttpSession> session;
	private Beanery beanery;
	private DbProvider dbProvider;
	private UserService userService;
	private UserValidator userValidator;

	@Inject
	public AuthServlet(Provider<HttpSession> sessions, UserService userService, DbProvider dbProvider,
			UserValidator userValidator) {
		this.session = sessions;
		this.userService = userService;
		this.dbProvider = dbProvider;
		this.userValidator = userValidator;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	/**
	 * This is the equivalent main method for this class
	 *
	 * @param req  req  Standard Http ServletRequest
	 * @param resp resp  Standard Http ServletResponse
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

		// get the json
		StringBuilder sb = new StringBuilder();
		BufferedReader br = req.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		br.close();

		log.info("SessionServlet signUp authBean json recieved" + sb.toString());

		AutoBean<AuthUserBean> authBean = AutoBeanCodex.decode(beanery, AuthUserBean.class, sb.toString());

		String action = authBean.as().getAction();
		log.info("SessionServlet signUp authBean.as().getAction() " + authBean.as().getAction());
		try {
			if (action.equals("signUp")) {
				doSignUp(resp, authBean);
			} else if (action.equals("auth")) {
				doAuth(resp, authBean);
			} else if (action.equals("sidAuth")) {
				doSidAuth(resp, authBean);
			} else if (action.equals("forgotPass")) {
				doForgotPass(resp, authBean);
			} else if (action.equals("changePass")) {
				doChangePass(resp, authBean);
			}
		} catch (RdlCodedException e) {
			log.info("Coded exception thrown: " + e.getClass().getName() + " code: " + e.getCode());
			PrintWriter out = resp.getWriter();
			out.write(e.getCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			PrintWriter out = resp.getWriter();
			out.write(RDLConstants.ErrorCodes.GENERIC);
		}

	} // end doPost

	private void doChangePass(HttpServletResponse resp, AutoBean<AuthUserBean> authBean)
			throws IOException, UserValidationException, TokenInvalidException {
		log.info("Session servlet - doChangePass");
		UserBean userBean = userValidator.validateCanChangePass(authBean);
		userService.changePass(userBean, authBean.as().getPassword());

		PrintWriter out = resp.getWriter();
		out.write("success");
	}


	private void doForgotPass(HttpServletResponse resp, AutoBean<AuthUserBean> authBean)
			throws IOException, InvalidInputException, RDLSendEmailException {
		String email = authBean.as().getEmail();
		//checked if the email is a registered user.
		UserBean userBean = userService.getUserByEmail(email);
		//if user not found
		if (userBean == null)
			throw new InvalidInputException();
		//if username not correct
		if (!userBean.getUsername().equals(authBean.as().getName()))
			throw new InvalidInputException();

		//generate new pass
		String newPass = ServerUtils.generatePassword();
		String newPassHash = ServerUtils.encryptString(newPass);

		//send the new password to the user's registered email
		EmailSender.sendNewPassEmail(newPass, email, dbProvider.getDb());

		//update user hash password
		userBean.setPassHash(newPassHash);

		//activate and set the login attempt to 0
		userBean.setStatus("Active");
		userBean.setLoginAttempt(0);
		//update the user
		userService.updateUser(userBean);


		//return an email since it is registered
		authBean.as().setEmail(userBean.getEmail());

		PrintWriter out = resp.getWriter();
		log.info("Writing output: " + AutoBeanCodex.encode(authBean).getPayload());
		out.write(AutoBeanCodex.encode(authBean).getPayload());
	}

	private void doSignUp(HttpServletResponse resp, AutoBean<AuthUserBean> authBean) throws IOException, UserValidationException {
		userValidator.validateAuthUserBean(authBean);
		AutoBean<UserBean> newUserBean = beanery.userBean();
		newUserBean.as().setEmail(authBean.as().getEmail());
		//generate a unique session ID
		newUserBean.as().setSid(ServerUtils.generateUUID());
		newUserBean.as().setUsername(authBean.as().getName());
		newUserBean.as().setDateCreated(new SimpleDateFormat(RDLConstants.DATE_PATTERN).format(new Date()));
		String password = authBean.as().getPassword();
		String hash = ServerUtils.encryptString(password);
		log.info("SessionServlet password hash = " + hash);
		newUserBean.as().setPassHash(hash);
		newUserBean.as().setRep(authBean.as().getRep());
		newUserBean.as().setLoginAttempt(0); //initialize login Attempt
		newUserBean.as().setStatus("Active");
		userService.createUser(newUserBean.as());
		authBean.as().setAuth(true);
		authBean.as().setAction("newUserOk");
		setSessionAttributes(authBean);
		log.info("SessionServlet signUp authBean" + AutoBeanCodex.encode(authBean).getPayload());
		PrintWriter out = resp.getWriter();
		out.write(AutoBeanCodex.encode(authBean).getPayload());
	}

	private void doAuth(HttpServletResponse resp, AutoBean<AuthUserBean> authBean)
			throws IOException, UserValidationException {
		String password = authBean.as().getPassword();
		// get the user from the database if exists
		AutoBean<AuthUserBean> checkedUser = userService.authUser(authBean.as(), password);

		processCheckedUser(checkedUser);
		setSessionAttributes(checkedUser);

		PrintWriter out = resp.getWriter();
		log.info("Writing output: " + AutoBeanCodex.encode(checkedUser).getPayload());
		out.write(AutoBeanCodex.encode(checkedUser).getPayload());
	}

	private void doSidAuth(HttpServletResponse resp, AutoBean<AuthUserBean> authBean)
			throws IOException, UserValidationException {
		AutoBean<AuthUserBean> checkedUser = userService.findUserBySid(authBean.as().getSid());

		processCheckedUser(checkedUser);
		setSessionAttributes(checkedUser);
		PrintWriter out = resp.getWriter();
		log.info("Writing output: " + AutoBeanCodex.encode(checkedUser).getPayload());
		out.write(AutoBeanCodex.encode(checkedUser).getPayload());
	}

	private void setSessionAttributes(AutoBean<AuthUserBean> userBean) {
		session.get().setAttribute("userid", userBean.as().getEmail());
		session.get().setAttribute("sid", userBean.as().getSid());
		session.get().setAttribute("username", userBean.as().getName());
		session.get().setAttribute("timeSend","");
		session.get().setAttribute("msgCtr",0);
		session.get().setAttribute("spamWarning",0);
		session.get().setAttribute("spamTimeStart","");
	}

	private void processCheckedUser(AutoBean<AuthUserBean> checkedUser) throws UserValidationException {
		log.info("processCheckedUser " + checkedUser.as().toString());
		if (checkedUser.as().getStatus()!=null && !checkedUser.as().getStatus().equals("Active"))
			throw new UserValidationException(RDLConstants.ErrorCodes.C014);
		if (checkedUser.as().getAction().equals("OkUser")) {

			checkedUser.as().setAuth(true);
			// we can use this server side to obtain userId from session
			session.get().setAttribute("userid", checkedUser.as().getEmail());
			session.get().setAttribute("name", checkedUser.as().getName());
			
		} else {
			throw new UserValidationException(RDLConstants.ErrorCodes.C006);
		}
	}

}