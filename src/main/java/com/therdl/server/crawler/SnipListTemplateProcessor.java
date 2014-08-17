package com.therdl.server.crawler;

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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles a crawl request for a list of snips
 */
@Singleton
public class SnipListTemplateProcessor {

	private static Logger log = Logger.getLogger(SnipListTemplateProcessor.class.getName());

	private SnipsService snipsService;
	private Beanery beanery;

	@Inject
	public SnipListTemplateProcessor(SnipsService snipsService) {
		this.snipsService = snipsService;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	public void doProcess(final PrintWriter out, String query, String moduleName) throws IOException {
		log.info("SnipListTemplateProcessor doProcess - BEGIN query: "+query);
		AutoBean<SnipBean> queryBean = RDLUtils.parseSearchToken(beanery, query, null);
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as());

		queryBean.as().setPageIndex(queryBean.as().getPageIndex()+1);
		SnipListTemplate template = new SnipListTemplate(snipList,
				RDLUtils.builtTokenFromBean(queryBean,moduleName, null), shouldRenderNextPage(snipList));

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/snipList.mustache");
		mustache.execute(out, template).flush();
	}

	private boolean shouldRenderNextPage (List<SnipBean> snipList){
		if (snipList == null || snipList.size() < Constants.DEFAULT_PAGE_SIZE){
			return false;
		}
		return true;
	}
}
