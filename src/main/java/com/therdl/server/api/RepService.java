package com.therdl.server.api;

import com.therdl.shared.beans.RepBean;

/**
 * This service handles user reputation operations
 */
public interface RepService {

	/**
	 * Searches for a rep given by a user for a snip
	 * @param snipId the snip to search for
	 * @param email the user to search for
	 * @return the rep entry if found, null otherwise
	 */
	RepBean getRep(String snipId, String email);

	/**
	 * Adds rep with the current date
	 * @param snipId the snip to add for
	 * @param email the user adding
	 * @return the rep newly added
	 */
	void addRep(String snipId, String email);
}
