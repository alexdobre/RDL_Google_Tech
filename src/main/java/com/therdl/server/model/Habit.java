package com.therdl.server.model;



/**
 * A habit is a description a user will write to detail what he or she 
 * has learned and applied from TheRDL materials
 * @author Alex
 *
 */

public class Habit extends Snip{

	private static final long serialVersionUID = 1L;
	
	/**
	 * The user who wrote this habit
	 */
	private UserProfile user;
	
	/**
	 * A habit is related to a core RDL category
	 */
	private RDLCat category;
}
