package com.therdl.server.restapi;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The snip dispatcher servlet is getting quite large so we place helper methods in here
 */
@Singleton
public class SnipServletHelper {
	final Logger log = LoggerFactory.getLogger(SnipDispatcherServlet.class);
	private Beanery beanery;
	private SnipsService snipsService;
	private static SimpleDateFormat sdf = new SimpleDateFormat(RDLConstants.DATE_PATTERN_EXTENDED);

	@Inject
	public SnipServletHelper(SnipsService snipsService) {
		beanery = AutoBeanFactorySource.create(Beanery.class);
		this.snipsService = snipsService;
	}

	/**
	 * Depending on the contents of the action bean and abusive content we pass the logic down one of several paths
	 *
	 * @param actionBean     the action bean
	 * @param abusiveContent the content in question retrieved from validation
	 */
	public void dispatchAbuseActionRequest(AutoBean<SnipBean> actionBean, SnipBean abusiveContent) {
		//if the abusive content is not yet in the tribunal - this means the request adds a new report
		if (!SnipType.TRIBUNAL.getSnipType().equals(abusiveContent.getSnipType())) {
			saveAbuseReport(actionBean, abusiveContent);
			//if the abusive content is in the tribunal then we can either save a comment or do a vote
			//so if the content is not empty then we save a comment without incrementing the counts
		} else if (actionBean.as().getContent() != null && !actionBean.as().getContent().isEmpty()) {
			saveAbuseComment(actionBean, abusiveContent, sdf);
			//finally if none of the above is true then we save a vote
		} else {
			saveAbuseVote(actionBean, abusiveContent);
		}
	}

	private void saveAbuseReport(AutoBean<SnipBean> actionBean, SnipBean abusiveContent) {
		log.info("saveAbuseReport begin " + actionBean.as());
		saveAbuseComment(actionBean, abusiveContent, sdf);

		//if the content is already in the tribunal this means that the report is just a comment so we return here
		if (SnipType.TRIBUNAL.getSnipType().equals(abusiveContent.getSnipType())) return;

		//we modify the content - firstly we increment the abuse counter
		incrementAbuseCount(true, abusiveContent);
		// if we reach 3 reports we send it to the tribunal
		//special case if the author is RDL
		if (abusiveContent.getAbuseCount() == 3 && !RDLConstants.RDL_OFFICIAL_USER.equals(abusiveContent.getAuthor())) {
			abusiveContent.setPreviousSnipType(abusiveContent.getSnipType());
			//run the expiry date forwards one week
			Calendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_WEEK, 7);
			abusiveContent.setVotingExpiresDate(sdf.format(cal.getTime()));
			//set expiry flag
			abusiveContent.setVotingExpired(false);
			abusiveContent.setSnipType(SnipType.TRIBUNAL.getSnipType());
		}
		//we add the author to the list of reporters
		addUserToList(actionBean.as().getAuthor(), abusiveContent, true);
		//we update the reported content
		snipsService.updateSnip(abusiveContent);

	}

	private void saveAbuseComment(AutoBean<SnipBean> actionBean, SnipBean abusiveContent, SimpleDateFormat sdf) {
		//we now create and save the report
		//- please note we must do this first cause the abusive content update will null the snipID (parent ID)
		AutoBean<SnipBean> report = beanery.snipBean();
		report.as().setParentSnip(abusiveContent.getId());
		report.as().setSnipType(SnipType.ABUSE_REPORT.getSnipType());
		report.as().setContent(actionBean.as().getContent());
		report.as().setCreationDate(sdf.format(new Date()));
		snipsService.createSnip(report.as());
	}

	/**
	 * Registers a vote for abuse. Increments the abuse count and registers the voter
	 * it looks at getIsAbuseReportedByUser / setIsAbuseReportedNoByUser to check if
	 * the vote was a YES or a NO
	 *
	 * @param actionBean     the action bean
	 * @param abusiveContent the content in question retrieved from validation
	 */
	private void saveAbuseVote(AutoBean<SnipBean> actionBean, SnipBean abusiveContent) {

		//vote yes or no is passed in getIsAbuseReportedByUser / setIsAbuseReportedNoByUser
		if (actionBean.as().getIsAbuseReportedByUser() != null && actionBean.as().getIsAbuseReportedByUser() == 1) {
			//user voted yes - we increment the yes vote number and att to the yes list
			incrementAbuseCount(true, abusiveContent);
			addUserToList(actionBean.as().getAuthor(), abusiveContent, true);
		} else {
			incrementAbuseCount(false, abusiveContent);
			addUserToList(actionBean.as().getAuthor(), abusiveContent, false);
		}

		//we update the snip to register the vote
		snipsService.updateSnip(abusiveContent);
	}

	private void addUserToList(String username, SnipBean abusiveContent, boolean votedYes) {
		if (votedYes) {
			if (abusiveContent.getAbuseReporters() == null) {
				abusiveContent.setAbuseReporters(new ArrayList<String>(1));
			}
			abusiveContent.getAbuseReporters().add(username);
		} else {
			if (abusiveContent.getAbuseReportersNo() == null) {
				abusiveContent.setAbuseReportersNo(new ArrayList<String>(1));
			}
			abusiveContent.getAbuseReportersNo().add(username);
		}
	}

	private void incrementAbuseCount(boolean increment, SnipBean abusiveContent) {
		if (increment) {
			if (abusiveContent.getAbuseCount() == null) {
				abusiveContent.setAbuseCount(1);
			} else {
				abusiveContent.setAbuseCount(abusiveContent.getAbuseCount() + 1);
			}
		} else {
			if (abusiveContent.getNoAbuseCount() == null) {
				abusiveContent.setNoAbuseCount(1);
			} else {
				abusiveContent.setNoAbuseCount(abusiveContent.getNoAbuseCount() + 1);
			}
		}
	}
}
