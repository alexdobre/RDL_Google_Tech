package com.therdl.server.model;


/**
 * This is a central part of the RDL democratic system
 * a user can rate and comment on snips so we can all find out
 * which of them are the best
 * @author Alex
 *
 */

public class Reference extends Snip{

	private static final long serialVersionUID = 1L;
	

	
	/**
	 * If the reference is positive or negative
	 * Neutral references are comments
	 */
	private Boolean positive;
	


	public Boolean getPositive() {
		return positive;
	}

	public void setPositive(Boolean positive) {
		this.positive = positive;
	}
	
}
