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
		log.info("SnipListTemplateProcessor doProcess - BEGIN query: " + query);
		AutoBean<SnipBean> queryBean = RDLUtils.parseSearchToken(beanery, query, null);
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as());

		queryBean.as().setPageIndex(queryBean.as().getPageIndex() + 1);
		SnipListTemplate template = new SnipListTemplate(snipList,
				RDLUtils.builtTokenFromBean(queryBean, moduleName, null), shouldRenderNextPage(snipList));

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/snipList.mustache");
		mustache.execute(out, template).flush();
	}

	private void doProcessWelcome(final PrintWriter out) throws IOException {
		AutoBean<SnipBean> queryBean = beanery.snipBean();
		RDLUtils.buildDefaultWelcomeBean(queryBean);
		queryBean.as().setSortOrder(1);
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as());

		SnipListTemplate template = new SnipListTemplate(snipList, null, false);

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/welcome.mustache");
		mustache.execute(out, template).flush();
	}

	private boolean shouldRenderNextPage(List<SnipBean> snipList) {
		if (snipList == null || snipList.size() < Constants.DEFAULT_PAGE_SIZE) {
			return false;
		}
		return true;
	}
}
