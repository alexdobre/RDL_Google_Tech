package com.therdl.shared;


/**
 * Standard Model View Presenter History tokens
 * see http://www.gwtproject.org/articles/mvp-architecture.html#history
 * @ String WELCOME history token for the landing page
 * @ String SNIPS history token for the snip search page
 * @ String SNIP_EDIT history token for the snip editor page
 * @ String LOG_OUT history token for the logged out page
 * @ String PROFILE history token for the user profile  page
 * @ String SNIP_VIEW history token for the user to view a snip page
 */
public interface RDLConstants {
	
	public interface Tokens {
		String WELCOME = "welcome";
		String SNIPS = "snips";
		String SNIP_EDIT = "snipEdit";
        String LOG_OUT = "logOut";
        String SIGN_UP = "signUp";
        String PROFILE = "profile";
        String SNIP_VIEW = "snipView";

	}
	

}
