package com.therdl.shared;

/**
 * These are the colour coded seven categories for RDL
 * they form the core of the business and together aim to cover most if not all 
 * relationship facets
 * @author Alex
 *
 */
public enum CoreCategory {
	
	COMPATIBILITY ("Compatibility","The axis of compatibility",1,"green"),
	SUPPORT ("Emmotional Support", "The skills of emmotional support",2,"yellow"),
	SENSIBILITIES("Sensibilities", "Balance sensibilities",3,"blue"),
	ATTRACTION("Attraction & Eroticism","The mechanisms of attraction and eroticism",4,"red"),
	
	SEDUCTION("Seduction","Seduction",5,"red"),
	PSY_TEND("Psy Tendencies","Common psychological tendencies and how they impact relationships",6,"violet"),
	AFFAIRS("Affairs","The mechanisms of affairs",7,"orange");

    private final String shortName;
    private final String name;
    private final int number;
    private final String colCode;

	CoreCategory(String shortName, String name, int number, String colCode){
        this.shortName = shortName;
        this.name = name;
        this.number = number;
        this.colCode = colCode;
	}

    public String getShortName() {
        return shortName;
    }
}
