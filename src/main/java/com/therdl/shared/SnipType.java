package com.therdl.shared;

/**
 * Holds all the possible snip types
 */
public enum SnipType {
	SNIP("snip"), FAST_CAP("fastCap"), MATERIAL("material"), HABIT("habit"), REFERENCE("reference"),
	THREAD("thread"), POST("post"),
	IMPROVEMENT("improvement"), PLEDGE("pledge"), COUNTER("counter"), PROPOSAL("proposal"),
	CONTENT_MGMT("contentMgmt"), PROFILE("profile");

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
	 * at the time of writing these are the Snips module and the Improvements module
	 *
	 * @param typeName
	 * @return
	 */
	public static Boolean isSpecialAccess(String typeName) {
		if (THREAD.getSnipType().equals(typeName) || POST.getSnipType().equals(typeName)) {
			return false;
		}
		return true;
	}

	/**
	 * Tests if the given string is the title of a snip type
	 * @param toTest the string to test
	 * @return true if the string is a core category title
	 */
	public static boolean stringIsType (String toTest){
		try {
			SnipType snipType =  SnipType.fromString(toTest);
			if (snipType != null) return true;
			else return false;
		} catch (Exception e){
			return false;
		}
	}

	public boolean isIdea() {
		if (this.equals(SNIP) || this.equals(FAST_CAP) || this.equals(HABIT) || this.equals(MATERIAL)) {
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
