package com.therdl.server.crawler;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.therdl.shared.RDLConstants;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Depending on the crawl request coming in, the dispatcher routes to the appropriate template logic
 */
@Singleton
public class CrawlerDispatcher {

	private SnipListTemplateProcessor listProcessor;
	private SnipViewTemplateProcessor viewProcessor;

	@Inject
	public CrawlerDispatcher(SnipListTemplateProcessor listProcessor, SnipViewTemplateProcessor viewProcessor) {
		this.listProcessor = listProcessor;
		this.viewProcessor = viewProcessor;
	}

	public void handleDispatch(final PrintWriter out, String query) throws IOException {
		String moduleName = null;
		boolean isView = false;
		if (query.contains("snips")) {
			moduleName = RDLConstants.Tokens.SNIPS;
		} else if (query.contains("stories")) {
			moduleName = RDLConstants.Tokens.STORIES;
		} else if (query.contains("improvements")) {
			moduleName = RDLConstants.Tokens.IMPROVEMENTS;
		} else if (query.contains("snipView")) {
			moduleName = RDLConstants.Tokens.SNIP_VIEW;
			isView = true;
		} else if (query.contains("threadView")) {
			moduleName = RDLConstants.Tokens.THREAD_VIEW;
			isView = true;
		} else if (query.contains("proposalView")) {
			moduleName = RDLConstants.Tokens.PROPOSAL_VIEW;
			isView = true;
		} else if (query.contains("welcome")) {
			moduleName = RDLConstants.Tokens.WELCOME;
		}

		if (moduleName != null) {
			if (isView) {
				viewProcessor.doProcess(out, query, moduleName);
			} else {
				listProcessor.doProcess(out, query, moduleName);
			}
		} else {
			//show error page
			MustacheFactory mf = new DefaultMustacheFactory();
			Mustache mustache = mf.compile("mustache/error.mustache");
			mustache.execute(out, null).flush();
		}
	}
}
