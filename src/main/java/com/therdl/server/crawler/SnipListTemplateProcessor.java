package com.therdl.server.crawler;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.client.view.common.SnipType;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.RDLConstants;
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

	public void doProcess(final PrintWriter out, String query) throws IOException {
		log.info("SnipListTemplateProcessor doProcess - BEGIN");
		AutoBean<SnipBean> queryBean = prepareSearchBean();
		List<SnipBean> snipList = snipsService.searchSnipsWith(queryBean.as(), 0);
		SnipListTemplate template = new SnipListTemplate(snipList, RDLConstants.Tokens.SNIPS, 1);

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/snipList.mustache");
		mustache.execute(out, template).flush();
	}

	private AutoBean<SnipBean> prepareSearchBean() {
		AutoBean<SnipBean> queryBean = beanery.snipBean();
		StringBuffer snipTypes = new StringBuffer();
		snipTypes.append(SnipType.SNIP.getSnipType()).append(',').
				append(SnipType.MATERIAL.getSnipType()).append(',').
				append(SnipType.FAST_CAP.getSnipType()).append(',').
				append(SnipType.HABIT.getSnipType()).append(',');
		queryBean.as().setSnipType(snipTypes.toString());
		//{"snipType":"snip,habit,fastCap,material","sortField":"creationDate","sortOrder":-1,"action":"search"}
		queryBean.as().setSortField("creationDate");
		queryBean.as().setSortOrder(-1);
		queryBean.as().setAction("search");
		return queryBean;
	}
}
