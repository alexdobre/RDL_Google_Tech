package com.therdl.server.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.SnipBean;

/**
 * The snip dispatcher servlet is getting quite large so we place helper methods in here
 */
@Singleton
public class SnipServletHelper {
	final Logger log = LoggerFactory.getLogger(SnipDispatcherServlet.class);

	public void saveAbuseReport(AutoBean<SnipBean> actionBean, SnipBean abusiveContent) {
		log.info("saveAbuseReport begin "+actionBean.as());
		//we modify the content - firstly we increment the abuse counter
		if (abusiveContent.getAbuseCount() == null){
			abusiveContent.setAbuseCount(1);
		} else {
			abusiveContent.setAbuseCount(abusiveContent.getAbuseCount()+1);
		}
		// if we reach 3 reports we send it to the tribunal
		if (abusiveContent.getAbuseCount() == 3){
			abusiveContent.setSnipType(RDLConstants.SnipType.TRIBUNAL);
		}
	}
}
