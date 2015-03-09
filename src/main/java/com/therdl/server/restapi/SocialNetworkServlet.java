package com.therdl.server.restapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.UserService;
import com.therdl.server.apiimpl.CredentialsService;
import com.therdl.server.data.AwsS3Credentials;
import com.therdl.server.data.SocialNetworkCredentials;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

@Singleton
public class SocialNetworkServlet extends HttpServlet{
	
	private Logger log = Logger.getLogger(SocialNetworkServlet.class);
	private final Provider<HttpSession> session;
	private CredentialsService credentialsService;
	private Beanery beanery;
	private UserService userService;
	
	@Inject
	public SocialNetworkServlet(Provider<HttpSession> session, UserService userService, CredentialsService credentialsService) {
		super();
		this.session = session;
		this.userService = userService;
		this.credentialsService = credentialsService;
		beanery = AutoBeanFactorySource.create(Beanery.class);
		SocialNetworkCredentials cred = this.credentialsService.getFaceBookCredentials();
		Constants.FACEBOOK_API_KEY= cred.getApiKey();
		
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		// get the json
				StringBuilder sb = new StringBuilder();
				BufferedReader br = req.getReader();
				String str;
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				br.close();
				log.info("SocialNetworkServlet: sb.toString()  " + sb.toString());
				PrintWriter out = resp.getWriter();
				// action bean as not all fields may have been set
//				AutoBean<SnipBean> actionBean = AutoBeanCodex.decode(beanery, SnipBean.class, sb.toString());
				sb.setLength(0);
				ArrayList<HashMap<String, String>> beanList = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> beanBag = new HashMap<String, String>();
				beanBag.put(Integer.toString(1), this.credentialsService.getFaceBookCredentials().getApiKey());
				beanList.add(beanBag);
				writeListToOutput(resp, beanList);
				
	}
	
	private void writeListToOutput(HttpServletResponse resp, ArrayList<HashMap<String, String>> beanList) throws IOException {
		Gson gson = new Gson();
		PrintWriter out = resp.getWriter();
		out.write(gson.toJson(beanList));
		beanList.clear();
	}
	
	

}
