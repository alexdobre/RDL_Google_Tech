package com.therdl.server.restapi;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Serves the Google custom search HTML
 */
@Singleton
public class GcseServlet extends HttpServlet {

	final Logger log = LoggerFactory.getLogger(GcseServlet.class);

	@Inject
	public GcseServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final PrintWriter out = resp.getWriter();
		doProcess(out);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//noop
	}

	public void doProcess(final PrintWriter out) throws IOException {
		log.info("GcseServlet doProcess - BEGIN");

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache/gcse.mustache");
		mustache.execute(out, null).flush();
	}
}
