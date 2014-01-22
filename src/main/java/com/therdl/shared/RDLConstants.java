package com.therdl.shared;


import java.util.HashMap;

/**
 * Standard Model View Presenter History tokens
 * see http://www.gwtproject.org/articles/mvp-architecture.html#history
 *
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

        String STORIES = "stories";
        String IMPROVEMENTS = "improvements";
        String THREAD_EDIT = "threadEdit";
        String THREAD_VIEW = "threadView";

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
        String POSTS = "posts";
        String SNIP_TYPE = "snipType";

        String SORT_ORDER = "sortOrder";
        String SORT_FIELD = "sortField";
    }

    public interface ReferenceType {
        String POSITIVE = "positive";
        String NEUTRAL = "neutral";
        String NEGATIVE = "negative";

        HashMap<String, String> colorCodes = new HashMap<String, String>() {{
            put(POSITIVE, "green");
            put(NEUTRAL, "gray");
            put(NEGATIVE, "red");
        }};
    }

    public interface SnipType {
        String SNIP = "snip";
        String HABIT = "habit";
        String FAST_CAP = "fastCap";
        String MATERIAL = "material";
        String REFERENCE = "reference";
        String THREAD = "thread";
        String POST = "post";
    }

    public interface SnipFields {
        String TITLE = "title";
        String CORE_CAT = "coreCat";
        String SUB_CAT = "subCat";
        String AUTHOR = "author";
        String VIEWS = "views";
        String POS_REF = "posRef";
        String NEUTRAL_REF = "neutralRef";
        String NEGATIVE_REF = "negativeRef";
        String POSTS = "posts";
        String REP = "rep";
        String CREATION_DATE = "creationDate";
    }

    public interface Modules {
        String IDEAS = "ideas";
        String STORIES = "stories";
    }
}
