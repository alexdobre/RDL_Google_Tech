package com.therdl.server.restapi;


import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.RepService;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.server.util.MessageThrottle;
import com.therdl.server.validator.SnipValidator;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.RdlCodedException;
import com.therdl.shared.exceptions.SnipValidationException;
import com.therdl.shared.exceptions.TokenInvalidException;
import org.slf4j.Logger;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SnipDispatcherServlet controller. This project uses the Guice injection
 * schema for beans, see http://code.google.com/p/google-guice/wiki/SpringComparison
 * if you are from the Spring framework space
 * SnipDispatcherServlet uses Guice to implement  the command pattern re Gang of 4 design patterns
 * see http://java.dzone.com/articles/design-patterns-command
 *
 * @ HttpSession sessions, Servlet 3 api session object, use this for the current user id
 * @ SnipsService snipsService  see com.therdl.server.apiimpl.SnipServiceImpl java doc
 * @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * for new developers important to understand GWT AutoBean client/server architecture
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanFactory
 */
@Singleton
public class SnipDispatcherServlet extends HttpServlet {

	final Logger log = LoggerFactory.getLogger(SnipDispatcherServlet.class);
	private final Provider<HttpSession> sessions;

	private SnipsService snipsService;
	private UserService userService;
	private SnipValidator snipValidator;
	private RepService repService;
	private SnipServletHelper helper;


	/**
	 * for message beans
	 */
	Beanery beanery;


	/**
	 * Guice injector
	 *
	 * @param sessions
	 * @param snipsService
	 */
	@Inject
	public SnipDispatcherServlet(Provider<HttpSession> sessions, SnipsService snipsService, UserService userService,
	                             SnipValidator snipValidator, RepService repService, SnipServletHelper helper) {
		this.sessions = sessions;
		this.snipsService = snipsService;
		this.userService = userService;
		this.snipValidator = snipValidator;
		this.repService = repService;
		this.helper = helper;
		beanery = AutoBeanFactorySource.create(Beanery.class);

	}

	/**
	 * When code is running in the Maven Jetty plugin (development) the uri for this method will be
	 * 'http://localhost:8080/rdl/getSnips' URL
	 * When code is running in the JBoss Application server (deployment) the uri for this method will be
	 * 'http://localhost:8080/therdl/rdl/getSnips' URL
	 *
	 * @param req  HttpServletRequest Standard Http ServletRequest
	 * @param resp HttpServletResponse Standard Http ServletResponse
	 * @throws ServletException
	 * @throws IOException      String debugString, for testing injection scheme is wired up OK
	 *                          AutoBean<SnipBean> actionBean see this video for a great explanation of 'actions' in the command pattern
	 *                          http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
	 *                          here the actionBean relates the users requested action
	 *                          see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex for serverside
	 *                          autobean serialisation
	 *                          Gson gson see http://code.google.com/p/google-gson/ for Gson serialaisation
	 */
	@Override
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
		log.info("SnipDispatcherServlet: sb.toString()  " + sb.toString());
		PrintWriter out = resp.getWriter();
		// action bean as not all fields may have been set
		AutoBean<SnipBean> actionBean = AutoBeanCodex.decode(beanery, SnipBean.class, sb.toString());
		sb.setLength(0);
		try {
			if (actionBean.as().getAction().equals(RDLConstants.SnipAction.SEARCH)) {
				doSearch(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.GET_SNIP)) {
				doGetSnip(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.VIEW_SNIP)) {
				doViewSnip(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.SAVE)) {
				out.write(doSave(actionBean));
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.UPDATE)) {
				out.write(doUpdate(actionBean));
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.DELETE)) {
				out.write(doDelete(actionBean));
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.SAVE_REF)) {
				doSaveRef(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.GET_REF)) {
				doGetRefList(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.GIVE_REP)) {
				doGiveRep(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.REPORT_ABUSE)) {
				doReportAbuse(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.GET_FAQ)) {
				doGetFaq(resp, actionBean);
			} else if (actionBean.as().getAction().equals(RDLConstants.SnipAction.SEARCH_ABUSE)) {
				doSearchAbuse(resp, actionBean);
			}
		} catch (RdlCodedException e) {
			log.info("Coded exception thrown: " + e.getClass().getName() + " code: " + e.getCode());
			out.write(e.getCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			out.write(RDLConstants.ErrorCodes.GENERIC);
		}
	}

	private void doGiveRep(HttpServletResponse resp, AutoBean<SnipBean> actionBean)
			throws IOException, TokenInvalidException, SnipValidationException {
		snipValidator.validateCanGiveRep(actionBean);
		String email = getCurrentUserEmail();
		String username = getCurrentUserName();

		AutoBean<UserBean.RepGivenBean> repGivenBean = beanery.userRepGivenBean();
		repGivenBean.as().setSnipId(actionBean.as().getId());
		repGivenBean.as().setDate(snipsService.makeTimeStamp());
		repService.addRep(repGivenBean.as().getSnipId(), email);

		SnipBean bean = snipsService.incrementCounter(actionBean.as().getId(), RDLConstants.SnipFields.REP, email, username);
		//we also add the rep to the user
		userService.incrementCounter(bean.getAuthor(), RDLConstants.SnipFields.REP);

		AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
		PrintWriter out = resp.getWriter();
		out.write(AutoBeanCodex.encode(autoBean).getPayload());
	}

	private void doReportAbuse(HttpServletResponse resp, AutoBean<SnipBean> actionBean)
			throws IOException, TokenInvalidException, SnipValidationException, ParseException {
		log.info("Do report abuse - BEGIN: " + actionBean.as());
		SnipBean contentBean = snipValidator.validateCanReportAbuse(actionBean);
		helper.dispatchAbuseActionRequest(actionBean, contentBean);
	}

	private void doGetRefList(HttpServletResponse resp, AutoBean<SnipBean> actionBean) throws IOException {
		String email = getCurrentUserEmail();
		List<SnipBean> beanReferences = snipsService.getReferences(actionBean.as(), email, getCurrentUserName());

		ArrayList<HashMap<String, String>> beanList = getBeanList(beanReferences);

		Gson gson = new Gson();
		log.info(gson.toJson(beanList));
		PrintWriter out = resp.getWriter();
		out.write(gson.toJson(beanList));
		beanList.clear();
		actionBean.as().setAction("dump");
	}

	private void doSaveRef(HttpServletResponse resp, AutoBean<SnipBean> actionBean)
			throws IOException, TokenInvalidException, SnipValidationException {
		log.info("SnipDispatcherServlet: saveReference");
		//snipValidator.validateCanPost(actionBean,sessions);
		snipValidator.validateCanSaveRef(actionBean);
		String email = getCurrentUserEmail();
		// increments reference counter of parent snip for reference type (positive/neutral/negative)
		String parentSnipId = actionBean.as().getParentSnip();

		String counterField = RDLConstants.SnipFields.POS_REF;
		if (actionBean.as().getReferenceType() != null) {
			if (actionBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEUTRAL))
				counterField = RDLConstants.SnipFields.NEUTRAL_REF;
			else if (actionBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEGATIVE))
				counterField = RDLConstants.SnipFields.NEGATIVE_REF;

		} else if (actionBean.as().getSnipType().equals(RDLConstants.SnipType.PLEDGE)) {
			counterField = RDLConstants.SnipFields.PLEDGES;
		} else if (actionBean.as().getSnipType().equals(RDLConstants.SnipType.COUNTER)) {
			counterField = RDLConstants.SnipFields.COUNTERS;
		} else if (actionBean.as().getSnipType().equals(RDLConstants.SnipType.POST)) {
			counterField = RDLConstants.SnipFields.POSTS;
		}

		snipsService.incrementCounter(parentSnipId, counterField, email, getCurrentUserName());

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
		if (!actionBean.as().getSnipType().equals(RDLConstants.SnipType.POST)) {
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

	private String doDelete(AutoBean<SnipBean> actionBean) throws TokenInvalidException, SnipValidationException {
		log.info("SnipDispatcherServlet: actionBean.as().getAction() delete " + actionBean.as().getAction());
		log.info("SnipDispatcherServlet:submitted bean for update recieved  " + actionBean.as().getId());
		snipValidator.validateCanDelete(actionBean);
		snipsService.deleteSnip(actionBean.as().getId());
		String toReturn = actionBean.as().getId();
		log.info("Delete snip returning with ID: " + toReturn);
		return toReturn;
	}

	private String doUpdate(AutoBean<SnipBean> actionBean) throws TokenInvalidException, SnipValidationException {
		log.info("SnipDispatcherServlet: actionBean.as().getAction() update " + actionBean.as().getAction());
		log.info("SnipDispatcherServlet:submitted bean for update recieved  " + actionBean.as().getTitle());
		snipValidator.validateSnipBean(actionBean);
		actionBean.as().setEditDate(snipsService.makeTimeStamp());
		String toReturn = snipsService.updateSnip(actionBean.as());
		log.info("Update snip returning with ID: " + toReturn);
		return toReturn;
	}

	private String doSave(AutoBean<SnipBean> actionBean) throws TokenInvalidException, SnipValidationException {
		log.info("SnipDispatcherServlet: actionBean.as().getAction() save " + actionBean.as().getAction());
		// action bean is actually a bean to be submitted for saving
		log.info("SnipDispatcherServlet:submitted bean for saving recieved  " + actionBean.as().getTitle());
		MessageThrottle throttle = new MessageThrottle(sessions.get());
		throttle.updateTimeSend();
		if (!throttle.canPostMessage()) {
			throw new SnipValidationException(RDLConstants.ErrorCodes.C015);
		}
		throttle.resetMessageCounter();
		if (throttle.isUserSpamming()) {
			userService.blockUser((String)sessions.get().getAttribute("userId"),"spam");
		}
		//snipValidator.validateCanPost(actionBean,sessions);
		snipValidator.validateSnipBean(actionBean);
		actionBean.as().setCreationDate(snipsService.makeTimeStamp());
		String toReturn = snipsService.createSnip(actionBean.as());
		log.info("Delete snip returning with ID: " + toReturn);
		return toReturn;
	}

	private void doViewSnip(HttpServletResponse resp, AutoBean<SnipBean> actionBean) throws IOException {
		String email = getCurrentUserEmail();
		log.info("SnipDispatcherServlet: actionBean.id " + actionBean.as().getId());
		SnipBean bean = snipsService.getSnip(actionBean.as().getId(), email, getCurrentUserName());
		if (bean == null) return;

		String viewerId = actionBean.as().getViewerId();
		if (viewerId != null) {
			bean.setIsRefGivenByUser(userService.isRefGivenForSnip(viewerId, actionBean.as().getId()));
		}

		AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
		PrintWriter out = resp.getWriter();
		out.write(AutoBeanCodex.encode(autoBean).getPayload());
	}

	private void doGetSnip(HttpServletResponse resp, AutoBean<SnipBean> actionBean) throws IOException {
		SnipBean bean = snipsService.getSnip(actionBean.as().getId(), getCurrentUserEmail(), getCurrentUserName());
		log.info("SnipDispatcherServlet: actionBean.id " + actionBean.as().getId());
		log.info("SnipDispatcherServlet: bean.id " + bean.getId());
		AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
		PrintWriter out = resp.getWriter();
		out.write(AutoBeanCodex.encode(autoBean).getPayload());
	}

	private void doSearch(HttpServletResponse resp, AutoBean<SnipBean> actionBean) throws IOException {
		List<SnipBean> beans = snipsService.searchSnipsWith(actionBean.as(), getCurrentUserEmail(), getCurrentUserName());
		log.info("SnipDispatcherServlet: beans.size() " + beans.size());
		log.info("SnipDispatcherServlet: actionBean.as().getAction() getall " + actionBean.as().getAction());

		ArrayList<HashMap<String, String>> beanList = getBeanList(beans);

		log.info("SnipDispatcherServlet: beanList.size() " + beanList.size());

		writeListToOutput(resp, beanList);
	}

	private void doGetFaq(HttpServletResponse resp, AutoBean<SnipBean> actionBean) throws IOException {
		//no validation cause everyone can view the FAQ list
		log.info("SnipDispatcherServlet - get FAQ");
		List<SnipBean> beans = helper.getFaqList();
		log.info("FAQ list size " + beans.size());
		ArrayList<HashMap<String, String>> beanList = getBeanList(beans);
		writeListToOutput(resp, beanList);
	}

	private void doSearchAbuse(HttpServletResponse resp, AutoBean<SnipBean> actionBean) throws IOException {
		List<SnipBean> beans = snipsService.searchAbuse(actionBean.as());
		log.info("SnipDispatcherServlet: beans.size() " + beans.size());
		log.info("SnipDispatcherServlet: actionBean.as().getAction() getAll " + actionBean.as().getAction());
		ArrayList<HashMap<String, String>> beanList = getBeanList(beans);
		log.info("SnipDispatcherServlet: beanList.size() " + beanList.size());
		writeListToOutput(resp, beanList);
	}

	private void writeListToOutput(HttpServletResponse resp, ArrayList<HashMap<String, String>> beanList) throws IOException {
		Gson gson = new Gson();
		PrintWriter out = resp.getWriter();
		out.write(gson.toJson(beanList));
		beanList.clear();
	}

	/**
	 * creates list of beans for response
	 *
	 * @param beans
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getBeanList(List<SnipBean> beans) {
		ArrayList<HashMap<String, String>> beanList = new ArrayList<HashMap<String, String>>();
		int k = 0;
		for (SnipBean bean : beans) {
			HashMap<String, String> beanBag = new HashMap<String, String>();
			AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
			String asJson = AutoBeanCodex.encode(autoBean).getPayload();
			beanBag.put(Integer.toString(k), asJson);
			beanList.add(beanBag);
			k++;
		}

		return beanList;
	}

	private String getCurrentUserEmail() {
		return (String) sessions.get().getAttribute("userid");
	}

	private String getCurrentUserName() {
		return (String) sessions.get().getAttribute("username");
	}

}

