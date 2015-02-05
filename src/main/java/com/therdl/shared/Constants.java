package com.therdl.shared;

/**
 * All shared constants could be placed here
 *
 * @DEPLOY boolean determine url for deployment mode
 */
public class Constants {
	//WARNING - DEFAULT_PAGE_SIZE must not be changed to below 10, we search for welcome snips which are 9 in number
	// if this limit was below 10 the google crawler will not be able to see all the welcome snips
	// please see com/therdl/server/crawler/SnipListTemplateProcessor.java -> doProcessWelcome
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final int DEFAULT_REFERENCE_PAGE_SIZE = 10;

	public static final String FACEBOOK_URL = "https://www.facebook.com/therdl";
	public static final String TWITTER_URL = "https://twitter.com/TheRDLCurator";
	public static final String YOUTUBE_URL = "https://www.youtube.com/channel/UCDRBFnE3tXLHY6mDLnRpA9g";

	public static final String MAIN_PAGE = "/rdl.html";
	public static final String ERROR_PAGE = "/error.jsp";

	public static final Integer MAXMESSAGESENT = 3;
	public static final String 	ADMINEMAIL = "alx.dobre@gmail.com";

	public static final Integer MESSAGETIMELIMIT = 1; //IN MINUTES
	public static final Integer SPAMTIMELIMTE = 60; //IN MINUTES;
}
