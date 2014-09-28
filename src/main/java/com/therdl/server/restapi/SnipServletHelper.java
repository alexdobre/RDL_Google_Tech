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

	@Inject
	public SnipServletHelper(SnipsService snipsService) {
		beanery = AutoBeanFactorySource.create(Beanery.class);
		this.snipsService = snipsService;
	}

	public void saveAbuseReport(AutoBean<SnipBean> actionBean, SnipBean abusiveContent) {
		log.info("saveAbuseReport begin " + actionBean.as());
		//we now create and save the report
		//- please note we must do this first cause the abusive content update will null the snipID (parent ID)
		AutoBean<SnipBean> report = beanery.snipBean();
		report.as().setParentSnip(abusiveContent.getId());
		report.as().setSnipType(SnipType.ABUSE_REPORT.getSnipType());
		report.as().setContent(actionBean.as().getContent());
		snipsService.createSnip(report.as());

		//we modify the content - firstly we increment the abuse counter
		if (abusiveContent.getAbuseCount() == null) {
			abusiveContent.setAbuseCount(1);
		} else {
			abusiveContent.setAbuseCount(abusiveContent.getAbuseCount() + 1);
		}
		// if we reach 3 reports we send it to the tribunal
		if (abusiveContent.getAbuseCount() == 3) {
			abusiveContent.setPreviousSnipType(abusiveContent.getSnipType());
			//run the expiry date forwards one week
			Calendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_WEEK, 7);
			abusiveContent.setVotingExpiresDate(new SimpleDateFormat(RDLConstants.DATE_PATTERN).format(cal.getTime()));
			//set expiry flag
			abusiveContent.setVotingExpired(false);
			abusiveContent.setSnipType(SnipType.TRIBUNAL.getSnipType());
		}
		//we add the author to the list of reporters
		if (abusiveContent.getAbuseReporters() == null) {
			abusiveContent.setAbuseReporters(new ArrayList<String>(1));
		}
		abusiveContent.getAbuseReporters().add(actionBean.as().getAuthor());
		//we update the reported content
		snipsService.updateSnip(abusiveContent);

	}
}
