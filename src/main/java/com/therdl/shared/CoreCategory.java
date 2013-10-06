package com.therdl.shared;

import java.util.EnumSet;

/**
 * These are the colour coded seven categories for RDL
 * they form the core of the business and together aim to cover most if not all 
 * relationship facets
 * @author Alex
 *
 */
public enum CoreCategory {
	
	COMPATIBILITY ("Compatibility","The axis of compatibility",1,"green", "#9ce148", EnumSet.range(SubCategory.FAMILY_FRIENDS, SubCategory.CAREER_HOBBIES)),
    CONNECTION ("Connection", "The skills of emotional support",2,"yellow", "#ffff00", null),
    EXTERIOR("Exterior", "Exterior",3,"blue", "#004cc0", null),
    EROTICISM("Eroticism","The mechanisms of attraction and eroticism",4,"red", "#f11515", null),
	
	SEDUCTION("Seduction","Seduction",5,"red", "#ed427d", null),
	PSY_TEND("Psy Tendencies","Common psychological tendencies and how they impact relationships",6,"violet", "#a32ad4", EnumSet.range(SubCategory.NPD, SubCategory.DISLEXIA)),
	AFFAIRS("Affairs","The mechanisms of affairs",7,"orange", "#ff8f00", null),
    ABUSE("Abuse", "Abuse", 8, "brown", "#d69051", null);

    private final String shortName;
    private final String name;
    private final int number;
    private final String colName;
    private final String colCode;
    private final EnumSet subCategories;

	CoreCategory(String shortName, String name, int number, String colName, String colCode, EnumSet subCategories){
        this.shortName = shortName;
        this.name = name;
        this.number = number;
        this.colName = colName;
        this.colCode = colCode;
        this.subCategories = subCategories;
	}

    public String getShortName() {
        return shortName;
    }

    public EnumSet getSubCategories() {
        return subCategories;
    }
}
