package com.therdl.server.model;


/**
 * Fast cap contains relationship advice attached to a category 
 * @author Alex
 *
 */

public class FastCap extends Snip {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The category under which this fast cap resides
	 */
	private FastCapCat category;

	public void setCategory(FastCapCat category) {
		this.category = category;
	}

	public FastCapCat getCategory() {
		return category;
	}
}
