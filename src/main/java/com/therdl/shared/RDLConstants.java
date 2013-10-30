package com.therdl.shared;


/**
 * Standard Model View Presenter History tokens
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
	
    public interface BookmarkSearch {
        String TITLE = "title";
        String CORE_CAT = "coreCat";
        String SUB_CAT = "subCat";
        String POS_REF = "posRef";
        String NEUTRAL_REF = "neutralRef";
        String NEGATIVE_REF = "negativeRef";
        String REP = "rep";
        String CONTENT = "content";
        String AUTHOR = "author";
        String DATE_FROM = "dateFrom";
        String DATE_TO = "dateTo";
    }
}
