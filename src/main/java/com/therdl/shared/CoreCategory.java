package com.therdl.shared;

/**
 * These are the colour coded eight categories for RDL
 * they form the core of the business and together aim to cover most if not all
 * relationship facets
 *
 * @author Alex
 */
public enum CoreCategory {

	COMPATIBILITY("Compatibility", "The axis of compatibility", 1, "green", "#9ce148"),
	CONNECTION("Connection", "The skills of emotional support", 2, "yellow", "#ffff00"),
	EXTERIOR("Exterior", "Exterior", 3, "blue", "#004cc0"),
	EROTICISM("Eroticism", "The mechanisms of attraction and eroticism", 4, "red", "#f11515"),

	SEDUCTION("Seduction", "Seduction", 5, "red", "#ed427d"),
	PSY_TEND("Psy Tendencies", "Common psychological tendencies and how they impact relationships", 6, "violet", "#a32ad4"),
	AFFAIRS("Affairs", "The mechanisms of affairs", 7, "orange", "#ff8f00"),
	ABUSE("Abuse", "Abuse", 8, "brown", "#d69051"),

	GENERAL("General", "Don't Know", 9, "grey", "#ffffff");

	private final String shortName;
	private final String name;
	private final int number;
	private final String colName;
	private final String colCode;

	CoreCategory(String shortName, String name, int number, String colName, String colCode) {
		this.shortName = shortName;
		this.name = name;
		this.number = number;
		this.colName = colName;
		this.colCode = colCode;
	}

	public String getShortName() {
		return shortName;
	}

	public String getColCode() {
		return colCode;
	}
}
