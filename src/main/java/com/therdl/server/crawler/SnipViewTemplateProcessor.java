package com.therdl.server.crawler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.therdl.shared.RDLConstants;
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
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

/**
 * Handles a crawl request for viewing a snip
 */
@Singleton
public class SnipViewTemplateProcessor {

	final Logger log = LoggerFactory.getLogger(SnipViewTemplateProcessor.class);

	private SnipsService snipsService;
	private Beanery beanery;

	@Inject
	public SnipViewTemplateProcessor(SnipsService snipsService) {
		this.snipsService = snipsService;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	public void doProcess(final PrintWriter out, String query, String moduleName) throws IOException {
		if (moduleName.equals(RDLConstants.Tokens.SERVICE_VIEW)) {
			doProcessServiceView(out, query, moduleName);
			return;
		}

		log.info("SnipViewTemplateProcessor - doProcess - BEGIN - query: " + query);
		SnipBean snipBean = snipsService.getSnip(extractId(query), null, null);
		AutoBean<SnipBean> queryBean = RDLUtils.parseSearchToken(beanery, query, snipBean.getId());
		queryBean.as().setReturnSnipContent(true);
		List<SnipBean> repliesList = snipsService.searchSnipsWith(queryBean.as(), null, null);

		queryBean.as().setPageIndex(queryBean.as().getPageIndex() + 1);
		SnipViewTemplate template = new SnipViewTemplate(snipBean, repliesList,
				RDLUtils.builtTokenFromBean(queryBean, moduleName, extractId(query)), shouldRenderNextPage(repliesList));

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/snipView.mustache");
		mustache.execute(out, template).flush();
	}

	private void doProcessServiceView(final PrintWriter out, String query, String moduleName) throws IOException {
		log.info("SnipViewTemplateProcessor Service- doProcess - BEGIN - query: " + query);
		SnipBean snipBean = snipsService.getSnip(extractId(query), null, null);
		AutoBean<SnipBean> queryBean = RDLUtils.parseSearchToken(beanery, query, snipBean.getId());
		queryBean.as().setReturnSnipContent(true);
		List<SnipBean> repliesList = snipsService.searchSnipsWith(queryBean.as(), null, null);

		queryBean.as().setPageIndex(queryBean.as().getPageIndex() + 1);
		ServiceViewTemplate template = new ServiceViewTemplate(snipBean, repliesList,
				RDLUtils.builtTokenFromBean(queryBean, moduleName, extractId(query)), shouldRenderNextPage(repliesList));

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/serviceView.mustache");
		mustache.execute(out, template).flush();
	}

	//the ID must always be the second item on the split
	private String extractId(String query) {
		String[] querySplit = query.split(":");
		return querySplit[1];
	}

	private boolean shouldRenderNextPage(List<SnipBean> snipList) {
		if (snipList == null || snipList.size() < Constants.DEFAULT_REFERENCE_PAGE_SIZE) {
			return false;
		}
		return true;
	}
}
