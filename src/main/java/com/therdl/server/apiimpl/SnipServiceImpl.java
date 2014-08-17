package com.therdl.server.apiimpl;


import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.therdl.server.api.SnipsService;
import com.therdl.server.data.DbProvider;
import com.therdl.server.validator.TokenValidator;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


/**
 * Snip crud operations
 *
 * @ String defaultDatabaseName, mongo database, in this case is 'rdl'
 * @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * for new developers important to understand GWT Autonean cliet/server architecture
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanFactory
 */

@Singleton
public class SnipServiceImpl implements SnipsService {

	private DbProvider dbProvider;
	private Beanery beanery;
	private TokenValidator tokenValidator;

	private static Logger log = Logger.getLogger(SnipServiceImpl.class.getName());

	@Inject
	public SnipServiceImpl(DbProvider dbProvider, TokenValidator tokenValidator) {
		this.dbProvider = dbProvider;
		this.tokenValidator = tokenValidator;
	}

	/**
	 * search snips for the given search options
	 *
	 * @param searchOptions search option data
	 * @return list of SnipBean
	 */
	@Override
	public List<SnipBean> searchSnipsWith(SnipBean searchOptions) {
		log.info("Searching snips with options: "+searchOptions);
		DB db = getMongo();
		List<SnipBean> beans = new ArrayList<SnipBean>();
		BasicDBObject query = new BasicDBObject();
		int pageIndex = searchOptions.getPageIndex();

		if (searchOptions.getParentSnip() != null)
			query.put("parentSnip",searchOptions.getParentSnip());
		if (searchOptions.getTitle() != null)
			query.put("title", java.util.regex.Pattern.compile(searchOptions.getTitle(), java.util.regex.Pattern.CASE_INSENSITIVE));
		if (searchOptions.getAuthor() != null) {
			if (!searchOptions.getAuthor().contains(",")){
				query.put("author", searchOptions.getAuthor());
			} else {
				query.put("author", new BasicDBObject("$in", searchOptions.getAuthor().split(",")));
			}
		}
		if (searchOptions.getCoreCat() != null){
			if (!searchOptions.getCoreCat().contains(",")){
				query.put("coreCat", searchOptions.getCoreCat());
			} else {
				query.put("coreCat", new BasicDBObject("$in", searchOptions.getCoreCat().split(",")));
			}
		}
		if (searchOptions.getPosRef() != null)
			query.put("posRef", new BasicDBObject("$gte", searchOptions.getPosRef()));
		if (searchOptions.getNeutralRef() != null)
			query.put("neutralRef", new BasicDBObject("$gte", searchOptions.getNeutralRef()));
		if (searchOptions.getNegativeRef() != null)
			query.put("negativeRef", new BasicDBObject("$gte", searchOptions.getNegativeRef()));
		if (searchOptions.getRep() != null)
			query.put("rep", new BasicDBObject("$gte", searchOptions.getRep()));
		if (searchOptions.getViews() != null)
			query.put("views", new BasicDBObject("$gte", searchOptions.getViews()));
		if (searchOptions.getPosts() != null)
			query.put("posts", new BasicDBObject("$gte", searchOptions.getPosts()));
		if (searchOptions.getSnipType() != null) {
			query.put("snipType", new BasicDBObject("$in", searchOptions.getSnipType().split(",")));
		}
		if (searchOptions.getProposalType() != null)
			query.put("proposalType", new BasicDBObject("$in", searchOptions.getProposalType().split(",")));
		if (searchOptions.getProposalState() != null)
			query.put("proposalState", new BasicDBObject("$in", searchOptions.getProposalState().split(",")));
		if (searchOptions.getPledges() != null)
			query.put("pledges", new BasicDBObject("$gte", searchOptions.getPledges()));
		if (searchOptions.getCounters() != null)
			query.put("counters", new BasicDBObject("$gte", searchOptions.getCounters()));

		if (searchOptions.getDateFrom() != null && searchOptions.getDateTo() != null) {
			query.put("creationDate", BasicDBObjectBuilder.start("$gte", searchOptions.getDateFrom())
					.add("$lte", searchOptions.getDateTo() + " 23:59:59").get());
		} else if (searchOptions.getDateFrom() != null) {
			query.put("creationDate", new BasicDBObject("$gte", searchOptions.getDateFrom()));
		} else if (searchOptions.getDateTo() != null) {
			query.put("creationDate", new BasicDBObject("$lte", searchOptions.getDateTo() + " 23:59:59"));
		}

		log.info("Search snips with query: "+query);
		DBCollection coll = db.getCollection("rdlSnipData");
		List<DBObject> objList = coll.find(query)
				.sort(new BasicDBObject(searchOptions.getSortField(), searchOptions.getSortOrder()))
				.skip((pageIndex) * Constants.DEFAULT_PAGE_SIZE)
				.limit(Constants.DEFAULT_PAGE_SIZE).toArray();

		for ( DBObject doc : objList){
			SnipBean snip = buildBeanObject(doc);
			beans.add(snip);
		}
		log.info("Found list size: "+beans.size());
		return beans;
	}

	/**
	 * crud get
	 * returns a snip ===  jpa find
	 *
	 * @return
	 */
	@Override
	public SnipBean getSnip(String id) {
		log.info("SnipServiceImpl getSnip  id: " + id);

		DB db = getMongo();
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		DBCollection coll = db.getCollection("rdlSnipData");
		DBCursor cursor = coll.find(query);
		DBObject doc = cursor.next();

		SnipBean snip = buildBeanObject(doc);
		log.info("Found snip: "+snip);
		return snip;
	}

	/**
	 * crud save
	 * saves a snip ===  jpa persist
	 *
	 * @param snip : Bean  to create
	 * @return returns id of the inserted record
	 */

	@Override
	public String createSnip(SnipBean snip) {
		log.info("SnipServiceImpl createSnip  title: " + snip.getTitle());
		DB db = getMongo();

		DBCollection coll = db.getCollection("rdlSnipData");
		if (snip.getId() == null) {
			BasicDBObject doc = buildDbObject(snip);
			coll.insert(doc);
			return doc.get("_id").toString();
		}

		return null;

	}

	/**
	 * crud update
	 * updates the snip
	 *
	 * @param snip : Bean  to update
	 * @return
	 */
	@Override
	public void updateSnip(SnipBean snip) {

		log.info("SnipServiceImpl updateSnip  updateSnip id: " + snip.getId());
		System.out.println("SnipServiceImpl updateSnip  updateSnip content " + snip.getId());
		System.out.println("SnipServiceImpl updateSnip  updateSnip content " + snip.getTitle());

		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlSnipData");

		// build the search query
		BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(snip.getId()));

		// set thee id to null to avoid exception from the driver (the id could not be updated)
		snip.setId(null);

		// get the db object to save
		BasicDBObject doc = buildDbObject(snip);

		// build the update query
		BasicDBObject updateDocument = new BasicDBObject();
		updateDocument.append("$set", doc);

		// make update
		coll.findAndModify(searchQuery, updateDocument);
	}

	/**
	 * increments counter for the given snip id
	 *
	 * @param id
	 * @param field to increment. This can be viewCount, rep or positive/neutral/negative reference count
	 * @return
	 */
	@Override
	public SnipBean incrementCounter(String id, String field) {
		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlSnipData");

		// build the search query
		BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(id));
		DBObject modifier = new BasicDBObject(field, 1);
		DBObject incQuery = new BasicDBObject("$inc", modifier);
		// make update
		DBObject dbObj = coll.findAndModify(searchQuery, incQuery);
		SnipBean snip = buildBeanObject(dbObj);
		return snip;
	}

	/**
	 * snip json contains an array of link objects representing references for the current snip
	 * adds a reference to that array for the snip with the given snip id
	 *
	 * @param linkAutoBean Link bean object
	 * @param parentSnipId parent snip id
	 * @return parent modified SnipBean
	 */
	public SnipBean addReference(AutoBean<SnipBean.Link> linkAutoBean, String parentSnipId) {
		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlSnipData");

		BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(parentSnipId));
		DBObject listItem = new BasicDBObject("links", new BasicDBObject("targetId", linkAutoBean.as().getTargetId()).append("rank", linkAutoBean.as().getRank()));
		DBObject updateQuery = new BasicDBObject("$push", listItem);
		DBObject dbObj = coll.findAndModify(searchQuery, updateQuery);
		SnipBean snip = buildBeanObject(dbObj);
		return snip;
	}

	/**
	 * finds references of the snip with the given id (id is in searchOptions bean) and filter
	 *
	 * @param searchOptions to filter references
	 * @return references as a list of SnipBean object
	 */
	public List<SnipBean> getReferences(SnipBean searchOptions, int pageIndex) {
		log.info("Snip Service getReferences - BEGIN");
		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlSnipData");

		List<SnipBean> beans = new ArrayList<SnipBean>();

		BasicDBObject query = new BasicDBObject();

		// filter references
		if (searchOptions.getId() != null){
			query.put("parentSnip", searchOptions.getId());
		}
		if (searchOptions.getReferenceType() != null){
			query.put("referenceType", new BasicDBObject("$in", searchOptions.getReferenceType().split(",")));
		}
		//   if(searchOptions.getRep() != null)
		//      query.put("rep", new BasicDBObject("$gte", searchOptions.getRep()));
		if (searchOptions.getAuthor() != null)
			query.put("author", searchOptions.getAuthor());
		if (searchOptions.getSnipType() != null)
			query.put("snipType", new BasicDBObject("$in", searchOptions.getSnipType().split(",")));

		log.info("Executing query author title and rep null: "+query);
		DBCursor collDocs = coll.find(query).sort(new BasicDBObject(searchOptions.getSortField(), searchOptions.getSortOrder())).skip((pageIndex) * Constants.DEFAULT_REFERENCE_PAGE_SIZE).limit(Constants.DEFAULT_REFERENCE_PAGE_SIZE);

		while (collDocs.hasNext()) {
			DBObject doc = collDocs.next();
			SnipBean snip = buildBeanObject(doc);
			beans.add(snip);
		}

		log.info("Returning result list size: "+beans.size());
		return beans;
	}

	/**
	 * this function is used when searching references/posts/pledges by author reputation or author title
	 *
	 * @param collDocs      DBCursor for the references
	 * @param searchOptions searchOptions
	 * @param query         query for snips
	 * @param pageIndex     index of page
	 * @return SnipBean list
	 */
	private List<SnipBean> filterReferencesByUser(DBCursor collDocs, SnipBean searchOptions, BasicDBObject query, int pageIndex) {
		DB db = getMongo();

		List<SnipBean> beans = new ArrayList<SnipBean>();

		// retrieve author names for the given snip list
		List<String> authors = new ArrayList<String>();
		while (collDocs.hasNext()) {
			DBObject doc = collDocs.next();
			if (!authors.contains(doc.get("author")))
				authors.add((String) doc.get("author"));

		}

		DBCollection collUser = db.getCollection("rdlUserData");
		DBCollection coll = db.getCollection("rdlSnipData");

		BasicDBObject queryUser = new BasicDBObject();
		queryUser.put("username", new BasicDBObject("$in", authors.toArray(new String[authors.size()])));

		// filter users by rep/title
		if (searchOptions.getAuthorRep() != null)
			queryUser.put("rep", new BasicDBObject("$gte", searchOptions.getAuthorRep()));
		if (searchOptions.getAuthorTitle() != null) {
			if (searchOptions.getAuthorTitle().equals(RDLConstants.UserTitle.RDL_DEV))
				queryUser.put("titles.titleName", RDLConstants.UserTitle.RDL_DEV);
			else if (searchOptions.getAuthorTitle().equals(RDLConstants.UserTitle.RDL_USER))
				queryUser.put("titles.titleName", new BasicDBObject("$ne", RDLConstants.UserTitle.RDL_DEV));
		}
		// query on user collection
		DBCursor collUsers = collUser.find(queryUser);

		// retrieve author names from result
		List<String> filteredAuthors = new ArrayList<String>();
		while (collUsers.hasNext()) {
			DBObject doc = collUsers.next();
			filteredAuthors.add((String) doc.get("username"));
		}
		// add filtered authors in snip query
		query.put("author", new BasicDBObject("$in", filteredAuthors.toArray(new String[filteredAuthors.size()])));

		// query to get snips for only filtered authors
		DBCursor collDocs1 = coll.find(query).sort(new BasicDBObject(searchOptions.getSortField(),
				searchOptions.getSortOrder())).skip((pageIndex) * Constants.DEFAULT_REFERENCE_PAGE_SIZE).
				limit(Constants.DEFAULT_REFERENCE_PAGE_SIZE);

		while (collDocs1.hasNext()) {
			DBObject doc = collDocs1.next();
			SnipBean snip1 = buildBeanObject(doc);
			beans.add(snip1);
		}

		return beans;
	}


	/**
	 * crud delete
	 * updates deletes a snip
	 *
	 * @param id snip to delete key
	 * @return
	 */
	@Override
	public void deleteSnip(String id) {
		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlSnipData");
		BasicDBObject deleteDocument = new BasicDBObject();
		deleteDocument.append("_id", new ObjectId(id));
		DBCursor cursor = coll.find(deleteDocument);
		while (cursor.hasNext()) {
			DBObject item = cursor.next();
			coll.remove(item);
		}
	}

	/**
	 * makes current timestamp
	 *
	 * @return current timestamp as String
	 */
	public String makeTimeStamp() {
		Date processDateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		String timeStampString = formatter.format(processDateTime);

		return timeStampString;
	}

	/**
	 * builds the db object from Bean
	 *
	 * @param : SnipBean snip
	 * @return : BasicDBObject
	 */
	private BasicDBObject buildDbObject(SnipBean snip) {

		BasicDBObject doc = new BasicDBObject();

		// if there is id in user instance , append it
		if (snip.getId() != null) {
			doc.append("_id", snip.getId());
		}
		doc.append("title", snip.getTitle());
		doc.append("content", snip.getContent());
		doc.append("author", snip.getAuthor());
		doc.append("creationDate", snip.getCreationDate());
		doc.append("editDate", snip.getEditDate());
		doc.append("snipType", snip.getSnipType());
		doc.append("coreCat", snip.getCoreCat());
		doc.append("parentSnip", snip.getParentSnip());
		doc.append("views", snip.getViews());
		doc.append("rep", snip.getRep());
		doc.append("posRef", snip.getPosRef());
		doc.append("neutralRef", snip.getNeutralRef());
		doc.append("negativeRef", snip.getNegativeRef());
		doc.append("referenceType", snip.getReferenceType());
		doc.append("parentSnip", snip.getParentSnip());
		doc.append("votes", snip.getVotes());
		doc.append("money", snip.getMoney());
		doc.append("posts", snip.getPosts());

		doc.append("pledges", snip.getPledges());
		doc.append("counters", snip.getCounters());
		doc.append("proposalType", snip.getProposalType());
		doc.append("proposalState", snip.getProposalState());

		BasicDBList links = new BasicDBList();

		if (snip.getLinks() == null) {
			snip.setLinks(new ArrayList<SnipBean.Link>());
		}

		for (SnipBean.Link link : snip.getLinks()) {
			BasicDBObject obj = new BasicDBObject("targetId", link.getTargetId()).
					append("rank", link.getRank());
			links.add(obj);
		}

		doc.append("links", links);

		return doc;
	}

	/**
	 * builds the Bean from he db object
	 *
	 * @param : DBObject doc
	 * @return : SnipBean
	 */
	private SnipBean buildBeanObject(DBObject doc) {

		if (beanery == null) beanery = AutoBeanFactorySource.create(Beanery.class);
		SnipBean snip = beanery.snipBean().as();
		// set the fields
		snip.setId(doc.get("_id").toString());

		snip.setRep(RDLUtils.parseInt(doc.get("rep")));
		snip.setAuthor((String) doc.get("author"));
		snip.setContent((String) doc.get("content"));
		snip.setCoreCat((String) doc.get("coreCat"));
		snip.setCreationDate((String) doc.get("creationDate"));
		snip.setEditDate((String) doc.get("editDate"));
		snip.setMoney((String) doc.get("money"));
		snip.setNegativeRef(RDLUtils.parseInt(doc.get("negativeRef")));
		snip.setNeutralRef(RDLUtils.parseInt(doc.get("neutralRef")));
		snip.setSnipType((String) doc.get("snipType"));
		snip.setViews(RDLUtils.parseInt(doc.get("views")));
		snip.setTitle((String) doc.get("title"));
		snip.setReferenceType((String) doc.get("referenceType"));
		snip.setPosRef(RDLUtils.parseInt(doc.get("posRef")));
		snip.setParentSnip((String) doc.get("parentSnip"));
		snip.setVotes((String) doc.get("votes"));
		snip.setParentSnip((String) doc.get("parentSnip"));
		snip.setPosts(RDLUtils.parseInt(doc.get("posts")));

		snip.setPledges(RDLUtils.parseInt(doc.get("pledges")));
		snip.setCounters(RDLUtils.parseInt(doc.get("counters")));
		snip.setProposalType((String) doc.get("proposalType"));
		snip.setProposalState((String) doc.get("proposalState"));

		List<SnipBean.Link> linkList = new ArrayList<SnipBean.Link>();

		// set the List<SnipBean.Links>
		BasicDBList links = (BasicDBList) doc.get("links");

		for (Object obj : links) {
			SnipBean.Link titlesBean = beanery.snipLinksBean().as();
			titlesBean.setTargetId((String) ((BasicDBObject) obj).get("targetId"));
			titlesBean.setRank((String) ((BasicDBObject) obj).get("rank"));
			linkList.add(titlesBean);
		}

		snip.setLinks(linkList);

		return snip;
	}

	/**
	 * MongoClient("localhost", 27017)
	 * later the above  url will be changed to a cloud based schema hence
	 * UnknownHostException  exception
	 *
	 * @return
	 */
	private DB getMongo() {
		return dbProvider.getDb();
	}
}
