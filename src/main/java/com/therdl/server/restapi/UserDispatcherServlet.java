package com.therdl.server.restapi;


import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.UserService;
import com.therdl.shared.beans.Beanery;
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
 * UserDispatcherServlet controller. This project uses the Guice injection
 * schema for beans, see http://code.google.com/p/google-guice/wiki/SpringComparison
 * if you are from the Spring framework space
 * <p/>
 * UserDispatcherServlet uses Guice to implement  the command pattern re Gang of 4 design patterns
 * see http://java.dzone.com/articles/design-patterns-command
 *
 * @ HttpSession sessions, Servlet 3 api session object, use this for the current user id
 * @ UserService userServicee  see com.therdl.server.apiimpl.UserServiceImpl java doc
 * @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * for new developers important to understand GWT Autonean cliet/server architecture
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanFactory
 */

@Singleton
public class UserDispatcherServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(UserDispatcherServlet.class.getName());
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
	 *
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
	 * When code is running in the Maven Jetty plugin (development) the uri for this method will be
	 * 'http://localhost:8080/rdl/getUsers' URL
	 * <p/>
	 * When code is running in the JBoss Application server (deployment) the uri for this method will be
	 * 'http://localhost:8080/therdl/rdl/getUsers' URL
	 *
	 * @param HttpServletRequest  req  Standard Http ServletRequest
	 * @param HttpServletResponse resp  Standard Http ServletResponse
	 * @throws ServletException
	 * @throws IOException      AutoBean<UserBean> actionBean see this video for a great explanation of 'actions' in the command pattern
	 *                          http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
	 *                          here the actionBean relates the users requested action
	 *                          see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex for serverside
	 *                          autobean serialisation
	 *                          <p/>
	 *                          Gson gson see http://code.google.com/p/google-gson/ for Gson serialaisation
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		log.info("UserDispatcherServlet: doPost : " + req);


//        PrintWriter out = resp.getWriter();
//        out.write("done");


		String debugString = userService.getDebugString();
		log.info("UserDispatcherServlet:  " + debugString);

		// get the json
		StringBuilder sb = new StringBuilder();
		BufferedReader br = req.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		br.close();

		// this is a user bean refactor !!
		AutoBean<UserBean> actionBean = AutoBeanCodex.decode(beanery, UserBean.class, sb.toString());
		sb.setLength(0);

		if (actionBean.as().getAction().equals("getall")) {
			List<UserBean> beans = userService.getAllUsers();
			log.info("UserDispatcherServlet: beans.size() " + beans.size());
			log.info("UserDispatcherServlet: actionBean.as().getAction() getall " + actionBean.as().getAction());
			ArrayList<HashMap<String, String>> beanList = new ArrayList<HashMap<String, String>>();
			int k = 0;
			for (UserBean bean : beans) {
				HashMap<String, String> beanBag = new HashMap<String, String>();
				AutoBean<UserBean> autoBean = AutoBeanUtils.getAutoBean(bean);
				String asJson = AutoBeanCodex.encode(autoBean).getPayload();
				beanBag.put(Integer.toString(k), asJson);
				beanList.add(beanBag);
				k++;
			}

			log.info("UserDispatcherServlet: beanList.size() " + beanList.size());

			Gson gson = new Gson();
			log.info(gson.toJson(beanList));
			PrintWriter out = resp.getWriter();
			out.write(gson.toJson(beanList));
			beanList.clear();
			actionBean.as().setAction("dump");
		} else if (actionBean.as().getAction().equals("save")) {
			log.info("UserDispatcherServlet: actionBean.as().getAction() save " + actionBean.as().getAction());
			// action bean is actually a bean to be submitted for saving
			log.info("UserDispatcherServlet:submitted bean for saving recieved  " + actionBean.as().getEmail());
			userService.createUser(actionBean.as());
		} else if (actionBean.as().getAction().equals("update")) {
			log.info("UserDispatcherServlet: actionBean.as().getAction() update " + actionBean.as().getAction());
			log.info("UserDispatcherServlet:submitted bean for update recieved  " + actionBean.as().getEmail());
			userService.updateUser(actionBean.as());
		} else if (actionBean.as().getAction().equals("delete")) {
			log.info("UserDispatcherServlet: actionBean.as().getAction() delete " + actionBean.as().getAction());
			log.info("UserDispatcherServlet:submitted bean for update recieved  " + actionBean.as().getId());
			userService.deleteUser(actionBean.as().getId());
		}
	}

}

