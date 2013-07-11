package com.therdl.server.model;

import java.util.ArrayList;



/**
 * A material is any medium to long format relationship information
 * submitted by users and candidates for TheRDL
 * @author Alex
 *
 */

public class Material extends Snip{

	private static final long serialVersionUID = 1L;
	
	/**
	 * The categories that this material refers to
	 */
	private ArrayList<RDLCat> categories;

	public void setCategories(ArrayList<RDLCat> categories) {
		this.categories = categories;
	}

	public ArrayList<RDLCat> getCategories() {
		return categories;
	}
}
