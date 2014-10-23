package com.therdl.shared;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;

/**
 * This class contains java utility methods to be used throughout the project
 */

public class RDLUtils {

	private static Logger log = Logger.getLogger(RDLUtils.class.getName());

	/**
	 * This method is an enhanced parseInt checking for null and empty strings
	 * Please note this still throws an exception if the string is malformed
	 *
	 * @param s the object to parse
	 * @return the parsed INT, 0 if string is null or empty
	 */
	public static Integer parseInt(Object s) {
		if (s == null)
			return 0;
		if (s != null && s.toString().isEmpty())
			;
		return Integer.parseInt(s.toString());
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals(""))
			return true;
		return false;
	}

	/**
	 * parses the token and creates searchOptionsBean bean object for search options
	 *
	 * @return searchOptionsBean
	 */
	public static AutoBean<SnipBean> parseSearchToken(Beanery beanery, String token, String parentId) {
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		if (parentId != null)
			searchOptionsBean.as().setParentSnip(parentId);
		String[] tokenSplit = token.split(":");
		if (tokenSplit.length > 2) {
			buildBeanFromToken(searchOptionsBean, tokenSplit);
		} else {
			buildDefaultBean(searchOptionsBean, tokenSplit[0]);
		}

		return searchOptionsBean;
	}

	private static void buildDefaultBean(AutoBean<SnipBean> searchOptionsBean, String token) {
		if (token.contains("_escaped_fragment_"))
			token = token.replace("_escaped_fragment_=", "!");
		if (token != null) {
			switch (token) {
				case RDLConstants.Tokens.SNIPS:
					buildDefaultSnipsBean(searchOptionsBean);
					break;
				case RDLConstants.Tokens.STORIES:
					buildDefaultStoriesBean(searchOptionsBean);
					break;
				case RDLConstants.Tokens.IMPROVEMENTS:
					buildDefaultImprovementsBean(searchOptionsBean);
					break;
				case RDLConstants.Tokens.THREAD_VIEW:
					buildDefaultThreadViewBean(searchOptionsBean);
					break;
				case RDLConstants.Tokens.SNIP_VIEW:
					buildDefaultViewBean(searchOptionsBean);
					break;
				case RDLConstants.Tokens.SERVICE_VIEW:
					buildDefaultViewBean(searchOptionsBean);
					break;
				case RDLConstants.Tokens.PROPOSAL_VIEW:
					buildDefaultViewBean(searchOptionsBean);
					break;
			}
		}
	}

	public static AutoBean<CurrentUserBean> resetCurrentUserBeanFields(AutoBean<CurrentUserBean> currentUserBean) {
		currentUserBean.as().setAuth(false);
		currentUserBean.as().setName("");
		currentUserBean.as().setEmail("");
		currentUserBean.as().setRegistered(false);
		return currentUserBean;
	}

	/**
	 * Extracts a snip ID from the token
	 *
	 * @param token the token to parse
	 * @return the ID or null if ID not found
	 */
	public static String extractCurrentSnipId(String token) {
		log.info("extractCurrentSnipId - current token: " + token);
		String[] tokenSplit = token.split(":");
		if (tokenSplit.length > 1)
			return tokenSplit[1];
		return null;
	}

	public static void buildDefaultWelcomeBean(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setAuthor("RDL");
		searchOptionsBean.as().setAction("search");
		searchOptionsBean.as().setPageIndex(0);
		searchOptionsBean.as().setSortField("creationDate");
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSnipType("snip");
		searchOptionsBean.as().setReturnSnipContent(true);
	}

	public static void buildDefaultViewBean(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSortOrder(1);
	}

	public static void buildDefaultThreadViewBean(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSortOrder(1);
		searchOptionsBean.as().setSnipType(RDLConstants.SnipType.POST);
	}

	public static void buildDefaultImprovementsBean(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSnipType(RDLConstants.SnipType.PROPOSAL);
	}

	public static void buildDefaultServicesBean(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSnipType(RDLConstants.SnipType.SERVICE);
	}

	public static void buildDefaultSnipsBean(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSnipType(RDLConstants.SnipType.SNIP + "," + RDLConstants.SnipType.HABIT + "," +
				RDLConstants.SnipType.FAST_CAP + "," + RDLConstants.SnipType.MATERIAL);
	}

	public static void buildDefaultStoriesBean(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSnipType(RDLConstants.SnipType.THREAD);
	}

	private static void buildBeanFromToken(AutoBean<SnipBean> searchOptionsBean, String[] tokenSplit) {
		for (int i = 1; i < tokenSplit.length; i++) {
			String[] keyVal = tokenSplit[i].split("=");
			if (keyVal[0].equals(RDLConstants.BookmarkSearch.PARENT)) {
				searchOptionsBean.as().setParentSnip(keyVal[1].replace("+", " "));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.TITLE)) {
				searchOptionsBean.as().setTitle(keyVal[1].replace("+", " "));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.CORE_CAT)) {
				searchOptionsBean.as().setCoreCat(keyVal[1].replace("+", " "));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.POS_REF)) {
				searchOptionsBean.as().setPosRef(Integer.parseInt(keyVal[1]));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.NEUTRAL_REF)) {
				searchOptionsBean.as().setNeutralRef(Integer.parseInt(keyVal[1]));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.POSTS)) {
				searchOptionsBean.as().setPosts(Integer.parseInt(keyVal[1]));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.REP)) {
				searchOptionsBean.as().setRep(Integer.parseInt(keyVal[1]));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.AUTHOR)) {
				searchOptionsBean.as().setAuthor(keyVal[1].replace("+", " "));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.DATE_FROM)) {
				searchOptionsBean.as().setDateFrom(keyVal[1]);
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.DATE_TO)) {
				searchOptionsBean.as().setDateTo(keyVal[1]);
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.SORT_FIELD)) {
				searchOptionsBean.as().setSortField(keyVal[1]);
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.SORT_ORDER)) {
				searchOptionsBean.as().setSortOrder(Integer.parseInt(keyVal[1]));
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.SNIP_TYPE)) {
				searchOptionsBean.as().setSnipType(keyVal[1]);
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.PROPOSAL_TYPE)) {
				searchOptionsBean.as().setProposalType(keyVal[1]);
			} else if (keyVal[0].equals(RDLConstants.BookmarkSearch.PROPOSAL_STATE)) {
				searchOptionsBean.as().setProposalState(keyVal[1]);
			} else {
				try {
					if (keyVal[0].equals(RDLConstants.BookmarkSearch.PAGE)) {
						searchOptionsBean.as().setPageIndex(Integer.parseInt(keyVal[1]));
					}
				} catch (Exception e) {
					searchOptionsBean.as().setPageIndex(0);
				}
			}
		}
		log.info("Built bean from token: " + searchOptionsBean.as());
	}

	public static String builtTokenFromBean(AutoBean<SnipBean> searchOptionsBean, String moduleName, String id) {
		StringBuilder sb = new StringBuilder(moduleName);
		if (id != null) {
			sb.append(':').append(id);
		}
		if (searchOptionsBean.as().getParentSnip() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.PARENT).append('=').append(searchOptionsBean.as().getParentSnip());
		}
		if (searchOptionsBean.as().getTitle() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.TITLE).append('=').append(searchOptionsBean.as().getTitle());
		}
		if (searchOptionsBean.as().getCoreCat() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.CORE_CAT).append('=').append(searchOptionsBean.as().getCoreCat());
		}
		if (searchOptionsBean.as().getPosRef() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.POS_REF).append('=').append(searchOptionsBean.as().getPosRef());
		}
		if (searchOptionsBean.as().getNeutralRef() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.NEUTRAL_REF).append('=').append(searchOptionsBean.as().getNeutralRef());
		}
		if (searchOptionsBean.as().getPosts() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.POSTS).append('=').append(searchOptionsBean.as().getPosts());
		}
		if (searchOptionsBean.as().getRep() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.REP).append('=').append(searchOptionsBean.as().getRep());
		}
		if (searchOptionsBean.as().getAuthor() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.AUTHOR).append('=').append(searchOptionsBean.as().getAuthor());
		}
		if (searchOptionsBean.as().getDateFrom() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.DATE_FROM).append('=').append(searchOptionsBean.as().getDateFrom());
		}
		if (searchOptionsBean.as().getDateTo() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.DATE_TO).append('=').append(searchOptionsBean.as().getDateTo());
		}
		if (searchOptionsBean.as().getSortField() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.SORT_FIELD).append('=').append(searchOptionsBean.as().getSortField());
		}
		if (searchOptionsBean.as().getSortOrder() != 0) {
			sb.append(':').append(RDLConstants.BookmarkSearch.SORT_ORDER).append('=').append(searchOptionsBean.as().getSortOrder());
		}
		if (searchOptionsBean.as().getSnipType() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.SNIP_TYPE).append('=').append(searchOptionsBean.as().getSnipType());
		}
		if (searchOptionsBean.as().getProposalType() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.PROPOSAL_TYPE).append('=').append(searchOptionsBean.as().getProposalType());
		}
		if (searchOptionsBean.as().getProposalState() != null) {
			sb.append(':').append(RDLConstants.BookmarkSearch.PROPOSAL_STATE).append('=').append(searchOptionsBean.as().getProposalState());
		}
		if (searchOptionsBean.as().getPageIndex() != -1) {
			sb.append(':').append(RDLConstants.BookmarkSearch.PAGE).append('=').append(searchOptionsBean.as().getPageIndex());
		}

		return sb.toString();
	}
}
