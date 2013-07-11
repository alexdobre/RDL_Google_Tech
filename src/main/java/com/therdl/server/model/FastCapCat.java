package com.therdl.server.model;


/**
 * Fast cap categories they are sub categories of the core RDL categories
 * @author Alex
 *
 */

public class FastCapCat extends Snip{

	private static final long serialVersionUID = 1L;
	
	/**
	 * The category under which this fast cap resides
	 */
	private String  category;

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}	
}