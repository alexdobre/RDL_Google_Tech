package com.therdl.shared;

/**
 * Holds all the possible snip types
 */
public enum SnipType {
	SNIP("snip"), FAST_CAP("fastCap"), MATERIAL("material"), HABIT("habit"), REFERENCE("reference"),
	THREAD("thread"), POST("post"),
	IMPROVEMENT("improvement"), PLEDGE("pledge"), COUNTER("counter"), PROPOSAL("proposal"),
	CONTENT_MGMT("contentMgmt"), PROFILE("profile"),
	TRIBUNAL("tribunal"), ABUSE_REPORT("abuseReport"),
	FAQ("faq"),
	SERVICE("service");

	private String snipType;

	private SnipType(String snipType) {
		this.snipType = snipType;
	}

	public String getSnipType() {
		return snipType;
	}

	public static SnipType fromString(String str) {
		if (str != null) {
			for (SnipType s : SnipType.values()) {
				if (str.equalsIgnoreCase(s.snipType)) {
					return s;
				}
			}
		}
		return null;
	}

	/**
	 * Determines if the snip type is part of the RDL supporter modules of the app
	 * at the time of writing these are the Services module and the Improvements module
	 * along with the user profile which can be modified at any time but will only be
	 * viewed by other users if RDL supporter
	 *
	 * @param typeName
	 * @return
	 */
	public static Boolean isSpecialAccess(String typeName) {
		if (IMPROVEMENT.getSnipType().equals(typeName) || PLEDGE.getSnipType().equals(typeName) ||
				COUNTER.getSnipType().equals(typeName) || PROPOSAL.getSnipType().equals(typeName) ||
				CONTENT_MGMT.getSnipType().equals(typeName) || FAQ.getSnipType().equals(typeName) ||
				PROFILE.getSnipType().equals(typeName) || SERVICE.getSnipType().equals(typeName)) {
			return true;
		}
		return false;
	}

	/**
	 * Tests if the given string is the title of a snip type
	 *
	 * @param toTest the string to test
	 * @return true if the string is a core category title
	 */
	public static boolean stringIsType(String toTest) {
		try {
			SnipType snipType = SnipType.fromString(toTest);
			if (snipType != null)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isIdea() {
		if (this.equals(SNIP) || this.equals(FAST_CAP) || this.equals(HABIT) || this.equals(MATERIAL)) {
			return true;
		}
		return false;
	}

	public boolean isService() {
		if (this.equals(SERVICE)) {
			return true;
		}
		return false;
	}

	public boolean isStory() {
		if (this.equals(THREAD) || this.equals(REFERENCE)) {
			return true;
		}
		return false;
	}

	public boolean isImprovement() {
		if (this.equals(IMPROVEMENT) || this.equals(PLEDGE)) {
			return true;
		}
		return false;
	}

	/**
	 * Some types can only be replies
	 *
	 * @return true if the snip type is a post, reference or pledge
	 */
	public boolean isReplyType() {
		if (this.equals(REFERENCE) || this.equals(POST) || this.equals(PLEDGE)) {
			return true;
		}
		return false;
	}
}
