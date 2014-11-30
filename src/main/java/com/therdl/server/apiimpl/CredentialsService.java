package com.therdl.server.apiimpl;

import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.therdl.server.data.AwsS3Credentials;
import com.therdl.server.data.CoinbaseCredentials;
import com.therdl.server.data.DbProvider;
import com.therdl.server.data.PaypalCredentials;

import javax.inject.Inject;

/**
 * Functionality to grab credentials from the DB
 */
@Singleton
public class CredentialsService {

	private DbProvider dbProvider;

	@Inject
	public CredentialsService(DbProvider dbProvider) {
		this.dbProvider = dbProvider;
	}

	public AwsS3Credentials getAwsS3Credentials() {

		DB db = dbProvider.getDb();
		//get the mail credentials from the DB
		DBCollection coll = db.getCollection("awsS3Credentials");

		BasicDBObject query = new BasicDBObject();
		query.put("uid", 1);

		DBCursor cursor = coll.find(query);
		DBObject doc = cursor.next();

		AwsS3Credentials cred = new AwsS3Credentials();
		cred.setAccesKey((String) doc.get("accesKey"));
		cred.setSecretKey((String) doc.get("secretKey"));

		return cred;
	}

	public PaypalCredentials getPaypalCredentials() {
		DB db = dbProvider.getDb();
		//get the mail credentials from the DB
		DBCollection coll = db.getCollection("paypalCredentials");

		BasicDBObject query = new BasicDBObject();
		query.put("uid", "2");

		DBCursor cursor = coll.find(query);
		DBObject doc = cursor.next();

		PaypalCredentials cred = new PaypalCredentials();
		cred.setMode((String) doc.get("mode"));
		cred.setPassword((String)doc.get("password"));
		cred.setSignature((String)doc.get("signature"));
		cred.setUsername((String)doc.get("username"));
		return cred;
	}

	public CoinbaseCredentials getCoinBaseCredentials() {
		DB db = dbProvider.getDb();
		//get the coinbase credentials from the DB
		DBCollection coll = db.getCollection("coinbaseCredentials");

		BasicDBObject query = new BasicDBObject();
		query.put("uid", "1");

		DBCursor cursor = coll.find(query);
		DBObject doc = cursor.next();

		CoinbaseCredentials cred = new CoinbaseCredentials();
		cred.setClientId((String) doc.get("client_id"));
		cred.setClientSecret((String) doc.get("client_secret"));
		return cred;
	}

}
