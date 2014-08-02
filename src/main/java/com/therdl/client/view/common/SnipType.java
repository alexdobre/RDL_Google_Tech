package com.therdl.client.view.common;

/**
 * Holds all the possible snip types
 */
public enum SnipType {
	SNIP("snip"), FAST_CAP("fastCap"), MATERIAL("material"), HABIT("habit"), REFERENCE("reference"),
	THREAD("thread"), POST("post"),
	IMPROVEMENT("improvement"), PLEDGE("pledge");

	private String snipType;

	private SnipType(String snipType) {
		this.snipType = snipType;
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
