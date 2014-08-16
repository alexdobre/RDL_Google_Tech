package com.therdl.server.crawler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
	public CrawlerDispatcher (SnipListTemplateProcessor listProcessor, SnipViewTemplateProcessor viewProcessor){
		this.listProcessor =  listProcessor;
		this.viewProcessor = viewProcessor;
	}

	public void handleDispatch(final PrintWriter out, String query) throws IOException {
		if (query.contains("snips") || query.contains("stories") || query.contains("improvements")){
			listProcessor.doProcess(out,query);
		}else {
			viewProcessor.doProcess(out,query);
		}
	}
}
