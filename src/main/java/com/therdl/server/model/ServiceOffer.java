package com.therdl.server.model;

import java.util.ArrayList;



/**
 * Users can offer their relationship related services on the site
 * @author Alex
 *
 */

public class ServiceOffer extends Snip{

	private static final long serialVersionUID = 1L;
		
	/**
	 * Service offers have their own categories
	 */
	private  ArrayList<ServiceOfferCat> categoryList;
	
	/**
	 * Where are you offering the service
	 */
	private Location location;

	public ArrayList<ServiceOfferCat> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(ArrayList<ServiceOfferCat> categoryList) {
		this.categoryList = categoryList;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
