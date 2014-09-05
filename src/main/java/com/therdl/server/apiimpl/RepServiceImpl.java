package com.therdl.server.apiimpl;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.therdl.server.api.RepService;
import com.therdl.server.data.DbProvider;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.RepBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Reputation operations
 */
@Singleton
public class RepServiceImpl implements RepService {
	final Logger log = LoggerFactory.getLogger(RepServiceImpl.class);

	private DbProvider dbProvider;
	private Beanery beanery;


	@Inject
	public RepServiceImpl(DbProvider dbProvider) {
		this.dbProvider = dbProvider;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	@Override
	public RepBean getRep(String snipId, String userId) {
		log.info("RepServiceImpl getRep snip : " + snipId + " user: " + userId);
		DB db = dbProvider.getDb();
		BasicDBObject query = new BasicDBObject();
		query.put("snipId", snipId);
		query.put("userId", userId);
		DBCollection coll = db.getCollection("rdlRep");
		DBCursor cursor = coll.find(query);

		if (cursor.hasNext()) {
			DBObject doc = cursor.next();
			RepBean repBean = buildBeanObject(doc);
			log.info("RepServiceImpl getRep END FOUND");
			return repBean;
		}

		log.info("RepServiceImpl getRep END NOT FOUND");
		return null;
	}

	@Override
	public void addRep(String snipId, String userEmail) {
		log.info("RepServiceImpl addRep  snip : " + snipId + " user: " );
		DB db = dbProvider.getDb();

		DBCollection coll = db.getCollection("rdlRep");

		BasicDBObject doc = buildDbObject(snipId, userEmail);
		coll.insert(doc);
	}

	private RepBean buildBeanObject(DBObject doc) {
		RepBean repBean = beanery.repBean().as();

		repBean.setId(doc.get("_id").toString());
		repBean.setSnipId((String) doc.get("snipId"));
		repBean.setUserId((String) doc.get("userId"));
		repBean.setDateCreated((String) doc.get("dateCreated"));

		return repBean;
	}

	private BasicDBObject buildDbObject(String snipId, String userEmail) {
		BasicDBObject doc = new BasicDBObject();

		doc.append("snipId", snipId);
		doc.append("userId", userEmail);

		SimpleDateFormat sdf = new SimpleDateFormat(RDLConstants.DATE_PATTERN);
		doc.append("dateCreated", sdf.format(new Date()));

		return doc;
	}
}
