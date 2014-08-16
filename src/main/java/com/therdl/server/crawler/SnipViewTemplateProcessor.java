package com.therdl.server.crawler;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Handles a crawl request for viewing a snip
 */
@Singleton
public class SnipViewTemplateProcessor {


	private static Logger log = Logger.getLogger(SnipViewTemplateProcessor.class.getName());

	private SnipsService snipsService;
	private Beanery beanery;

	@Inject
	public SnipViewTemplateProcessor(SnipsService snipsService) {
		this.snipsService = snipsService;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	public void doProcess(final PrintWriter out, String query) throws IOException {
		log.info("SnipViewTemplateProcessor - doProcess - BEGIN - query: " + query);
		SnipBean snipBean = snipsService.getSnip(extractId(query));

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/snipView.mustache");
		mustache.execute(out, new SnipViewTemplate(snipBean)).flush();
	}

	private String extractId(String query) {
		return query.substring(query.lastIndexOf(':') + 1, query.length());
	}
}
