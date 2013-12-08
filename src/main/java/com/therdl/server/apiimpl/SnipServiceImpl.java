package com.therdl.server.apiimpl;


import com.google.common.collect.Iterables;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.mongodb.*;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.Constants;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;


import javax.inject.Singleton;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;


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

    private String defaultDatabaseName;
    private Beanery beanery;

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(SnipServiceImpl.class);


    /**
     * drops the collection
     */
    @Override
    public void dropSnipCollection() {
        DB db = getMongo();
        db.getCollection("rdlSnipData").drop();
    }


    /**
     * crud get
     * returns all snips ===  jpa findAll
     * @param pageIndex
     * @return
     */
    @Override
    public List<SnipBean> getAllSnips(int pageIndex) {
        DB db = getMongo();
        List<SnipBean> beans = new ArrayList<SnipBean>();

        DBCollection coll = db.getCollection("rdlSnipData");

        BasicDBObject query = new BasicDBObject();
        query.put("snipType", new BasicDBObject("$ne", RDLConstants.SnipType.REFERENCE));
        int collCount = coll.find(query).count();
        DBCursor collDocs = coll.find(query).sort(new BasicDBObject("creationDate", -1)).skip((pageIndex)*Constants.DEFAULT_PAGE_SIZE).limit(Constants.DEFAULT_PAGE_SIZE);

        while (collDocs.hasNext()) {
            DBObject doc = collDocs.next();
            SnipBean snip = buildBeanObject(doc);
            snip.setCount(collCount);
            beans.add(snip);
        }

        return beans;
    }

    /**
     * search snips for the given search options
     * @param searchOptions search option data
     * @param pageIndex
     * @return list of SnipBean
     */
    @Override
    public List<SnipBean> searchSnipsWith(SnipBean searchOptions, int pageIndex) {
        DB db = getMongo();
        List<SnipBean> beans = new ArrayList<SnipBean>();
        BasicDBObject query = new BasicDBObject();

        query.put("snipType", new BasicDBObject("$ne", RDLConstants.SnipType.REFERENCE));

        if (searchOptions.getTitle() != null)
            query.put("title", java.util.regex.Pattern.compile(searchOptions.getTitle()));
        if (searchOptions.getAuthor() != null)
            query.put("author", searchOptions.getAuthor());
        if (searchOptions.getContent() != null)
            query.put("content", java.util.regex.Pattern.compile(searchOptions.getContent()));
        if (searchOptions.getCoreCat() != null)
            query.put("coreCat", searchOptions.getCoreCat());
        if (searchOptions.getSubCat() != null)
            query.put("subCat", searchOptions.getSubCat());
        if (searchOptions.getPosRef() != null)
            query.put("posRef", new BasicDBObject("$gte", searchOptions.getPosRef()));
        if (searchOptions.getNeutralRef() != null)
            query.put("neutralRef", new BasicDBObject("$gte", searchOptions.getNeutralRef()));
        if (searchOptions.getNegativeRef() != null)
            query.put("negativeRef", new BasicDBObject("$gte", searchOptions.getNegativeRef()));
        if (searchOptions.getRep() != null) {
            query.put("rep", new BasicDBObject("$gte", searchOptions.getRep()));
        }

        if (searchOptions.getDateFrom() != null && searchOptions.getDateTo() != null) {
            query.put("creationDate", BasicDBObjectBuilder.start("$gte", searchOptions.getDateFrom())
                    .add("$lte", searchOptions.getDateTo() + " 23:59:59").get());
        } else if (searchOptions.getDateFrom() != null) {
            query.put("creationDate", new BasicDBObject("$gte", searchOptions.getDateFrom()));
        } else if (searchOptions.getDateTo() != null) {
            query.put("creationDate", new BasicDBObject("$lte", searchOptions.getDateTo() + " 23:59:59"));
        }


        DBCollection coll = db.getCollection("rdlSnipData");
        int collCount = coll.find(query).count();
        DBCursor cursor = coll.find(query).skip((pageIndex)*Constants.DEFAULT_PAGE_SIZE).limit(Constants.DEFAULT_PAGE_SIZE);;

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            SnipBean snip = buildBeanObject(doc);
            snip.setCount(collCount);
            beans.add(snip);
        }
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
        sLogger.info("SnipServiceImpl getSnip  id: " + id);

        DB db = getMongo();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCollection coll = db.getCollection("rdlSnipData");
        DBCursor cursor = coll.find(query);
        DBObject doc = cursor.next();

        SnipBean snip = buildBeanObject(doc);

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
        sLogger.info("SnipServiceImpl createSnip  title: " + snip.getTitle());
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

        sLogger.info("SnipServiceImpl updateSnip  updateSnip id: " + snip.getId());
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
     * @param linkAutoBean Link bean object
     * @param parentSnipId parent snip id
     * @return parent modified SnipBean
     */
    public SnipBean addReference(AutoBean<SnipBean.Link> linkAutoBean, String parentSnipId) {
        DB db = getMongo();
        DBCollection coll = db.getCollection("rdlSnipData");

        BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(parentSnipId));
        DBObject listItem = new BasicDBObject("links", new BasicDBObject("targetId",linkAutoBean.as().getTargetId()).append("rank",linkAutoBean.as().getRank()));
        DBObject updateQuery = new BasicDBObject("$push", listItem);
        DBObject dbObj = coll.findAndModify(searchQuery, updateQuery);
        SnipBean snip = buildBeanObject(dbObj);
        return snip;
    }

    /**
     * finds references of the snip with the given id
     * @param id snip id
     * @param referenceType filter by reference type, could be more than 1 reference type
     * @return references as a list of SnipBean object
     */
    public List<SnipBean> getReferences(String id, String referenceType) {
        DB db = getMongo();
        DBCollection coll = db.getCollection("rdlSnipData");

        List<SnipBean> beans = new ArrayList<SnipBean>();

        // first find a snip with the given id
        SnipBean snipBean = getSnip(id);

        // retrieve reference ids from the links nested array of the snip json
        List<ObjectId> referenceIds = new ArrayList<ObjectId>();
        for (int i=0; i<snipBean.getLinks().size(); i++) {
            referenceIds.add(new ObjectId(snipBean.getLinks().get(i).getTargetId()));
        }

        // query to get references from snip collection for the retrieved ids
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new BasicDBObject("$in", referenceIds));

        // referenceType is a list of reference types (pos/neg/neut) separated by comma, when referenceType is not empty filter also by reference type
        if(!referenceType.equals("")) {
           query.put("referenceType", new BasicDBObject("$in", referenceType.split(",")));
        }

        DBCursor collDocs = coll.find(query).sort(new BasicDBObject("creationDate", -1));

        while (collDocs.hasNext()) {
            DBObject doc = collDocs.next();
            SnipBean snip = buildBeanObject(doc);
            beans.add(snip);
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

    @Override
    public String getDebugString() {
        return "Snip Service wired up for guice injection ok";
    }

    /**
     * makes current timestamp
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
        //new appended field  avatarUrl
        doc.append("avatarUrl", snip.getAvatarUrl());
        doc.append("title", snip.getTitle());
        doc.append("content", snip.getContent());
        doc.append("author", snip.getAuthor());
        doc.append("creationDate", snip.getCreationDate());
        doc.append("editDate", snip.getEditDate());
        doc.append("snipType", snip.getSnipType());
        doc.append("coreCat", snip.getCoreCat());
        doc.append("subCat", snip.getSubCat());
        doc.append("views", snip.getViews());
        doc.append("rep", snip.getRep());
        doc.append("posRef", snip.getPosRef());
        doc.append("neutralRef", snip.getNeutralRef());
        doc.append("negativeRef", snip.getNegativeRef());
        doc.append("referenceType", snip.getReferenceType());
        doc.append("parentStream", snip.getParentStream());
        doc.append("parentTag", snip.getParentTag());
        doc.append("parentThread", snip.getParentThread());
        doc.append("votes", snip.getVotes());
        doc.append("money", snip.getMoney());

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
        //new appended field  avatarUrl
        snip.setAvatarUrl((String) doc.get("avatarUrl"));
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
        snip.setParentStream((String) doc.get("parentStream"));
        snip.setSnipType((String) doc.get("snipType"));
        snip.setViews(RDLUtils.parseInt(doc.get("views")));
        snip.setTitle((String) doc.get("title"));
        snip.setReferenceType((String) doc.get("referenceType"));
        snip.setPosRef(RDLUtils.parseInt( doc.get("posRef")));
        snip.setSubCat((String) doc.get("subCat"));
        snip.setParentTag((String) doc.get("parentTag"));
        snip.setVotes((String) doc.get("votes"));
        snip.setParentThread((String) doc.get("parentThread"));

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

        defaultDatabaseName = "rdl";

        try {
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB(defaultDatabaseName);
            return db;

        } catch (UnknownHostException e) {
            sLogger.error(e.getMessage());
            return null;
        }
    }
}
