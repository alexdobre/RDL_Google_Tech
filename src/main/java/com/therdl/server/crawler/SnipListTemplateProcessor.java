package com.therdl.server.crawler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

/**
 * Handles a crawl request for a list of snips
 */
@Singleton
public class SnipListTemplateProcessor {

	final Logger log = LoggerFactory.getLogger(SnipListTemplateProcessor.class);

	private SnipsService snipsService;
	private Beanery beanery;

	@Inject
	public SnipListTemplateProcessor(SnipsService snipsService) {
		this.snipsService = snipsService;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	public void doProcess(final PrintWriter out, String query, String moduleName) throws IOException {
		if (moduleName.equals(RDLConstants.Tokens.WELCOME)) {
			doProcessWelcome(out);
			return;
		}

		if (moduleName.equals(RDLConstants.Tokens.LICENSE)) {
			doProcessLicense(out);
			return;
		}

		if (moduleName.equals(RDLConstants.Tokens.FAQ)) {
			doProcessFaq(out);
			return;
		}

		if (moduleName.contains("service")) {
			log.info("SnipListTemplateProcessor Service doProcess - BEGIN query: " + query);
			AutoBean<SnipBean> queryBean = RDLUtils.parseSearchToken(beanery, query, null);
			List<SnipBean> serviceList = snipsService.searchSnipsWith(queryBean.as(), null, null);

			queryBean.as().setPageIndex(queryBean.as().getPageIndex() + 1);
			ServiceListTemplate template = new ServiceListTemplate(serviceList,
					RDLUtils.builtTokenFromBean(queryBean, moduleName, null),
					translateModuleToView(moduleName), shouldRenderNextPage(serviceList));

			MustacheFactory mf = new DefaultMustacheFactory();
			Mustache mustache = mf.compile("mustache/serviceList.mustache");
			mustache.execute(out, template).flush();
			return;
		}

		log.info("SnipListTemplateProcessor doProcess - BEGIN query: " + query);
		AutoBean<SnipBean> queryBean = RDLUtils.parseSearchToken(beanery, query, null);
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as(), null, null);

		queryBean.as().setPageIndex(queryBean.as().getPageIndex() + 1);
		SnipListTemplate template = new SnipListTemplate(snipList,
				RDLUtils.builtTokenFromBean(queryBean, moduleName, null),
				translateModuleToView(moduleName), shouldRenderNextPage(snipList));

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/snipList.mustache");
		mustache.execute(out, template).flush();
	}

	private void doProcessWelcome(final PrintWriter out) throws IOException {
		AutoBean<SnipBean> queryBean = beanery.snipBean();
		RDLUtils.buildDefaultWelcomeBean(queryBean);
		queryBean.as().setSortOrder(1);
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as(), null, null);

		SnipListTemplate template = new SnipListTemplate(snipList,
				translateModuleToView(RDLConstants.Tokens.WELCOME), null, false);

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/welcome.mustache");
		mustache.execute(out, template).flush();
	}

	private void doProcessLicense(final PrintWriter out) throws IOException {
		AutoBean<SnipBean> queryBean = beanery.snipBean();
		RDLUtils.buildLicenseBean(queryBean);
		queryBean.as().setSortOrder(1);
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as(), null, null);

		SnipListTemplate template = new SnipListTemplate(snipList,
				translateModuleToView(RDLConstants.Tokens.LICENSE), null, false);

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/license.mustache");
		mustache.execute(out, template).flush();
	}

	private void doProcessFaq(final PrintWriter out) throws IOException {
		AutoBean<SnipBean> queryBean = beanery.snipBean();
		RDLUtils.buildFaqBean(queryBean);
		queryBean.as().setSortOrder(1);
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as(), null, null);

		SnipListTemplate template = new SnipListTemplate(snipList,
				translateModuleToView(RDLConstants.Tokens.FAQ), null, false);

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/faq.mustache");
		mustache.execute(out, template).flush();
	}

	private boolean shouldRenderNextPage(List<SnipBean> snipList) {
		if (snipList == null || snipList.size() < Constants.DEFAULT_PAGE_SIZE) {
			return false;
		}
		return true;
	}

	private String translateModuleToView (String moduleName) {
		switch (moduleName) {
			case RDLConstants.Tokens.WELCOME: return RDLConstants.Tokens.SNIP_VIEW;
			case RDLConstants.Tokens.SNIPS: return RDLConstants.Tokens.SNIP_VIEW;
			case RDLConstants.Tokens.STORIES: return RDLConstants.Tokens.THREAD_VIEW;
			case RDLConstants.Tokens.IMPROVEMENTS: return RDLConstants.Tokens.PROPOSAL_VIEW;
			case RDLConstants.Tokens.LICENSE: return RDLConstants.Tokens.LICENSE_VIEW;
			case RDLConstants.Tokens.FAQ: return RDLConstants.Tokens.FAQ_VIEW;
			case RDLConstants.Tokens.SERVICES: return RDLConstants.Tokens.SERVICE_VIEW;
			default: return RDLConstants.Tokens.SNIP_VIEW;
		}
	}
}
