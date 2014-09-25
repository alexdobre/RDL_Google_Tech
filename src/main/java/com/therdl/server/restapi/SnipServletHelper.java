package com.therdl.server.restapi;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

/**
 * The snip dispatcher servlet is getting quite large so we place helper methods in here
 */
@Singleton
public class SnipServletHelper {
	final Logger log = LoggerFactory.getLogger(SnipDispatcherServlet.class);
	private Beanery beanery;

	public SnipServletHelper() {
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	public void saveAbuseReport(AutoBean<SnipBean> actionBean, SnipBean abusiveContent) {
		log.info("saveAbuseReport begin " + actionBean.as());
		//we modify the content - firstly we increment the abuse counter
		if (abusiveContent.getAbuseCount() == null) {
			abusiveContent.setAbuseCount(1);
		} else {
			abusiveContent.setAbuseCount(abusiveContent.getAbuseCount() + 1);
		}
		// if we reach 3 reports we send it to the tribunal
		if (abusiveContent.getAbuseCount() == 3) {
			abusiveContent.setSnipType(SnipType.TRIBUNAL.getSnipType());
		}
		//we add the author to the list of reporters
		if (abusiveContent.getAbuseReporters() == null) {
			abusiveContent.setAbuseReporters(new ArrayList<String>(1));
		}
		abusiveContent.getAbuseReporters().add(actionBean.as().getAuthor());
		//we update the reported content
		//TODO update reported abusive content
		//we now create and save the report
		AutoBean<SnipBean> report = beanery.snipBean();
		report.as().setParentSnip(abusiveContent.getId());
		report.as().setSnipType(SnipType.ABUSE_REPORT.getSnipType());
		report.as().setContent(actionBean.as().getContent());
		//TODO save abuse report
	}
}
