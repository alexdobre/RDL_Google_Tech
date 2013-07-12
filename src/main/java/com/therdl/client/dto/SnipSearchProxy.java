package com.therdl.client.dto;

import java.util.Date;

import com.therdl.server.model.SnipSearch;

/**
 * Contains the search parameters for a Snip
 * @author Alex
 *
 */

public interface SnipSearchProxy extends SnipProxy {
	
	public void setDateStop(Date dateStop);

	public Date getDateStop();
}
