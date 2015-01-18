package com.therdl.shared;


import java.util.HashMap;

import com.therdl.client.RDL;

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

	String DATE_PATTERN = "yyyy-MM-dd";
	String DATE_PATTERN_EXTENDED = "yyyy-MM-dd HH:mm:ss";
	String DATE_PATTERN_HYPER_EXTENDED = "yyyy-MM-dd HH:mm:ss.SSSS";
	String INTERNAL = "internal";
	String LICENSE_TITLE = "RDL License";
	String RDL_OFFICIAL_USER = "RDL";

	public interface Tokens {
		String WELCOME = "!welcome";
		String SUBSCRIBE = "supporter";
		String SERVICES = "!services";
		String SNIPS = "!snips";
		String SNIP_EDIT = "snipEdit";
		String SERVICE_EDIT = "serviceEdit";
		String LOG_OUT = "logOut";
		String SIGN_UP = "signUp";
		String PROFILE = "profile";
		String PUBLIC_PROFILE = "publicProfile";
		String SNIP_VIEW = "!snipView";
		String SERVICE_VIEW = "!serviceView";
		String LICENSE = "!license";
		String LICENSE_VIEW = "licenseView";
		String FAQ = "!faq";
		String FAQ_VIEW = "faqView";
		String ERROR = "error";

		String STORIES = "!stories";
		String TRIBUNAL = "tribunal";
		String IMPROVEMENTS = "!improvements";
		String THREAD_EDIT = "threadEdit";
		String THREAD_VIEW = "!threadView";

		String PROPOSAL_EDIT = "proposalEdit";
		String PROPOSAL_VIEW = "!proposalView";

		String TRIBUNAL_DETAIL = "tribunalDetail";
		String CONTENT_SEARCH = "contentSearch";
	}

	public interface SnipAction {
		String SEARCH = "search";
		String GET_SNIP = "getSnip";
		String VIEW_SNIP = "populateSnip";
		String SAVE = "save";
		String UPDATE = "update";
		String DELETE = "delete";
		String SAVE_REF = "saveReference";
		String GET_REF = "getReferences";
		String GIVE_REP = "giveRep";
		String REPORT_ABUSE = "reportAbuse";
		String SEARCH_ABUSE = "searchAbuse";
		String GET_FAQ = "getFaq";

		String SNIP_SERVLET_URL = "getSnips";
	}

	public interface BookmarkSearch {
		String TITLE = "title";
		String CORE_CAT = "coreCat";
		String POS_REF = "posRef";
		String NEUTRAL_REF = "neutralRef";
		String NEGATIVE_REF = "negativeRef";
		String REP = "rep";
		String AUTHOR = "author";
		String DATE_FROM = "dateFrom";
		String DATE_TO = "dateTo";
		String POSTS = "posts";
		String SNIP_TYPE = "snipType";
		String PARENT = "parent";
		String PAGE = "page";

		String PROPOSAL_TYPE = "proposalType";
		String PROPOSAL_STATE = "proposalState";

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
		String SERVICE = "service";
		String SNIP = "snip";
		String HABIT = "habit";
		String FAST_CAP = "fastCap";
		String MATERIAL = "material";
		String REFERENCE = "reference";
		String THREAD = "thread";
		String POST = "post";
		String PROPOSAL = "proposal";
		String PLEDGE = "pledge";
		String COUNTER = "counter";
		String TRIBUNAL = "tribunal";
	}

	public interface SnipFields {
		String TITLE = "title";
		String CORE_CAT = "coreCat";
		String AUTHOR = "author";
		String VIEWS = "views";
		String POS_REF = "posRef";
		String NEUTRAL_REF = "neutralRef";
		String NEGATIVE_REF = "negativeRef";
		String POSTS = "posts";
		String PLEDGES = "pledges";
		String COUNTERS = "counters";
		String REP = "rep";
		String CREATION_DATE = "creationDate";
	}

	public interface Modules {
		String WELCOME = "welcome";
		String SERVICES = "services";
		String IDEAS = "ideas";
		String STORIES = "stories";
		String IMPROVEMENTS = "Improvements";
		String TRIBUNAL = "tribunal";
		String LICENSE = "license";
		String FAQ = "faq";
	}

	public interface ProposalType {
		String NEW_FEATURE = "newFeature";
		String IMPROVEMENT = "improvement";
		String BUG = "bug";

		HashMap<String, String> proposalTypeHm = new HashMap<String, String>() {{
			put(NEW_FEATURE, RDL.i18n.newFeature());
			put(IMPROVEMENT, RDL.i18n.improvement());
			put(BUG, RDL.i18n.bug());
		}};
	}

	public interface ProposalState {
		String NEW = "NEW";
		String DEV_REPLIED = "DEV_REPLIED";
		String IN_PROGRESS = "IN_PROGRESS";
		String DONE = "DONE";
		String PARKED = "PARKED";

		HashMap<String, String> proposalStateHm = new HashMap<String, String>() {{
			put(NEW, RDL.i18n.propStateNew());
			put(DEV_REPLIED, RDL.i18n.propStateDevReplied());
			put(IN_PROGRESS, RDL.i18n.propStateInProgress());
			put(DONE, RDL.i18n.propStateDone());
			put(PARKED, RDL.i18n.propStateParked());
		}};
	}

	public interface UserTitle {
		String RDL_SUPPORTER = "RDL Supporter";
		String RDL_DEV = "RDL Dev";
		String RDL_USER = "RDL User";
		String NEVER_EXPIRES = "never";
	}

	public interface ContentMgmt {
		String OFFICIAL_AUTHOR = "RDL";
		String RDL_SUPP_TITLE = "RDL Supporter Title";
		String RDL_WELCOME_TITLE = "The RDL welcome message";
	}

	public interface ErrorCodes {
		String GENERIC = "c000";
		String C001 = "c001";
		String C002 = "c002";
		String C003 = "c003";
		String C004 = "c004";
		String C005 = "c005";
		String C006 = "c006";
		String C007 = "c007";
		String C008 = "c008";
		String C009 = "c009";
		String C010 = "c010";
		String C011 = "c011";
		String C012 = "c012";
		String C013 = "c013";
	}
}
