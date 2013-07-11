package com.therdl.client.dto;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.therdl.server.model.SnipSearch;

/**
 * Contains the search parameters for a Snip
 * @author Alex
 *
 */
@ProxyFor(value = SnipSearch.class)
public interface SnipSearchProxy extends SnipProxy {
	
	public void setDateStop(Date dateStop);

	public Date getDateStop();
}
