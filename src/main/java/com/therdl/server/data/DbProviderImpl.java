package com.therdl.server.data;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * The class holds a single instance of the mongo client to be used across the app
 */
@Singleton
public class DbProviderImpl implements DbProvider {

	final Logger log = LoggerFactory.getLogger(DbProviderImpl.class);

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
			log.error(e.getMessage());
			return null;
		}
	}

}
