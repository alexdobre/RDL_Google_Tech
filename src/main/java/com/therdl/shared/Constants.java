package com.therdl.shared;

/**
 * All shared constants could be placed here
 *
 * @DEPLOY boolean determine url for deployment mode
 */
public class Constants {

	/**
	 * Please change the DEPLOY constant to configure the paths of the
	 * projects for jetty and jboss
	 * <p/>
	 * true  : will optimize the paths for jboss this is the deployed mode
	 * false : will optimize the paths for jetty this is the development mode
	 */
	public static final int DEFAULT_PAGE_SIZE = 5;
	public static final int DEFAULT_REFERENCE_PAGE_SIZE = 5;

	public static final String FACEBOOK_URL = "https://www.facebook.com/therdl";
	public static final String TWITTER_URL = "https://twitter.com/RDLSocial";
	public static final String YOUTUBE_URL = "http://www.youtube.com/user/AlexTheRDL";
	public static final String REDDIT_URL = "http://www.reddit.com/user/RDLSocial";

	public static final String MAIN_PAGE = "/rdl.html";
	public static final String ERROR_PAGE = "/error.jsp";
}
