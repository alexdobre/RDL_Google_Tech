package com.therdl.server.crawler;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Singleton
public class CrawlFilter implements Filter {
	private static Logger log = Logger.getLogger(CrawlFilter.class.getName());

	private CrawlerDispatcher dispatcher;

	@Inject
	public CrawlFilter(CrawlerDispatcher crawlerDispatcher) {
		this.dispatcher = crawlerDispatcher;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final String queryString = httpRequest.getQueryString();

		if ((queryString != null) && queryString.contains("_escaped_fragment_")) {
			final PrintWriter out = httpResponse.getWriter();
			httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
			dispatcher.handleDispatch(out, queryString);
		} else {
			// not an _escaped_fragment_ URL, so move up the chain of servlet (filters)
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}