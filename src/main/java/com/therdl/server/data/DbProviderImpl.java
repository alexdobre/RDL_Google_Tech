package com.therdl.server.data;

import com.google.inject.Singleton;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * The class holds a single instance of the mongo client to be used across the app
 */
@Singleton
public class DbProviderImpl implements DbProvider {

	private static Logger log = Logger.getLogger(DbProviderImpl.class.getName());

	private static final String DEFAULT_DB_NAME = "rdl";

	private MongoClient mongoClient;

	public DB getDb() {
		try {
			if (mongoClient == null) {
				mongoClient = new MongoClient("localhost", 27017);
			}

			DB db = mongoClient.getDB(DEFAULT_DB_NAME);
			return db;

		} catch (UnknownHostException e) {
			log.severe(e.getMessage());
			return null;
		}
	}

}
