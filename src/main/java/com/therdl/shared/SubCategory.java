package com.therdl.shared;

public enum SubCategory {

	FAMILY_FRIENDS("Family Friends"),
	PARENTING("Parenting"),
	FINANCES("Finances"),
	CAREER_HOBBIES("Career Hobbies"),
	NPD("NPD"),
	DEPRESSION("Depression"),
	POST_TRAUMATIC_STRESS("Post traumatic stress"),
	ADDICTION("Addiction"),
	COMPULSIVE_LYING("Compulsive lying"),
	CODEPENDENCY("Codependency"),
	LOW_SELF_ESTEEM("Low self esteem"),
	OBSESSIVE_COMP_DIS("Obsessive compulsive disorder"),
	SULK_SIL_TREAT("Sulking/silent treatment"),
	HOARDING("Hoarding"),
	PASSIVE_AGGRESSIVE("Passive aggressive"),
	AUTISM("Autism"),
	ASPERGERS("Aspergers"),
	RAPE("Rape"),
	DISLEXIA("Dislexia");


	private final String name;

	SubCategory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
