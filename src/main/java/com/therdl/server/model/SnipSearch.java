package com.therdl.server.model;

import java.util.Date;

/**
 * This is not an entity subclass as it is never persisted.
 * This contains the search parameters for a Snip
 * @author Alex
 *
 */
public class SnipSearch extends Snip{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * we use the Snip date as the start of the search interval and the dateStop as the stop of the interval
	 */
	private Date dateStop;

	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
	}

	public Date getDateStop() {
		return dateStop;
	}
	
	/**
	 * RequestFactory requires this I don't know why
	 * @param id
	 * @return
	 */
	public static Snip findSnipSearch (Long id){
		return null;
	}

}
