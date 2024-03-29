package com.therdl.shared;

import java.util.logging.Logger;

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

	/**
	 * Tests if the given string is the title of a category
	 * @param toTest the string to test
	 * @return true if the string is a core category title
	 */
	public static boolean stringIsCateg (String toTest){
		//special case for Psy tend
		if (toTest.toLowerCase().equals(PSY_TEND.getShortName().toLowerCase())) return true;
		//special case for improvement
		if (toTest.equals(RDLConstants.Modules.IMPROVEMENTS)) return true;
		//special case for internal
		if (toTest.equals(RDLConstants.INTERNAL)) return true;
		try {
			CoreCategory coreCat =  CoreCategory.valueOf(toTest.toUpperCase());
			if (coreCat != null) return true;
			else return false;
		} catch (Exception e){
			return false;
		}
	}

	public static CoreCategory fromString(String str) {
		try {
			return CoreCategory.valueOf(str);
		} catch (Exception e) {
			return GENERAL;
		}
	}

	public String getShortName() {
		return shortName;
	}

	public String getColCode() {
		return colCode;
	}
}
