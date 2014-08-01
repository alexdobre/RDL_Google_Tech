package com.therdl.server.data;

import com.mongodb.DB;

/**
 * Interface currently holds the MongoClient
 */
public interface DbProvider {

	/**
	 * Get a DB instance
	 *
	 * @return the DB or null if error occurs
	 */
	public DB getDb();
}
