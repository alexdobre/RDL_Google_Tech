package com.therdl.server.apiimpl;


import com.google.common.collect.Iterables;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.mongodb.*;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;


import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Snip crud operations
 *  @ String defaultDatabaseName, mongo database, in this case is 'rdl'
 *  @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 *  for new developers important to understand GWT Autonean cliet/server architecture
 *  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex
 *  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanFactory
 *
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
    public void dropSnipCollection () {
        DB db = getMongo();
        db.getCollection("rdlSnipData").drop();
    }


    /**
     * crud get
     * returns all snips ===  jpa findAll
     * @return
     */
    @Override
    public List<SnipBean> getAllSnips() {
        DB db = getMongo();
        List<SnipBean> beans = new ArrayList<SnipBean>();
        beanery = AutoBeanFactorySource.create(Beanery.class);
        Set<String> colls = db.getCollectionNames();
        for (String s : colls) {
            sLogger.info(s);
        }
        DBCollection coll = db.getCollection("rdlSnipData");

        DBCursor collDocs  =  coll.find().sort(new BasicDBObject("creationDate", -1));

        while(collDocs.hasNext()) {
            DBObject doc = collDocs.next();
            SnipBean snip = buildBeanObject(doc);
            beans.add(snip);
        }

        return beans;
    }

    /**
     *  this is Serine/alex/artur code
     *  nigel suggested not to mix snip crud code with search code and create a search service class
     * @param searchOptions  search data
     * @return
     */
    @Override
    public List<SnipBean> searchSnipsWith(SnipBean searchOptions) {
        DB db = getMongo();
        List<SnipBean> beans = new ArrayList<SnipBean>();
        BasicDBObject query = new BasicDBObject();

        if(searchOptions.getTitle() != null)
            query.put("title",  java.util.regex.Pattern.compile(searchOptions.getTitle()));
        if(searchOptions.getAuthor() != null)
            query.put("author", searchOptions.getAuthor());
        if(searchOptions.getContent() != null)
            query.put("content", java.util.regex.Pattern.compile(searchOptions.getContent()));
        if(searchOptions.getCoreCat() != null)
            query.put("coreCat", searchOptions.getCoreCat());
        if(searchOptions.getSubCat() != null)
            query.put("subCat", searchOptions.getSubCat());
        if(searchOptions.getPosRef() != null)
            query.put("posRef", new BasicDBObject("$gte", searchOptions.getPosRef()));
        if(searchOptions.getNeutralRef() != null)
            query.put("neutralRef", new BasicDBObject("$gte", searchOptions.getNeutralRef()));
        if(searchOptions.getNegativeRef() != null)
            query.put("negativeRef", new BasicDBObject("$gte", searchOptions.getNegativeRef()));
        if(searchOptions.getRep() != null) {
            query.put("rep", new BasicDBObject("$gte", searchOptions.getRep()));
        }

        if(searchOptions.getDateFrom() != null && searchOptions.getDateTo() != null) {
            query.put("creationDate", BasicDBObjectBuilder.start("$gte", searchOptions.getDateFrom())
                    .add("$lte", searchOptions.getDateTo()+" 23:59:59").get());
        } else if(searchOptions.getDateFrom() != null) {
            query.put("creationDate", new BasicDBObject("$gte", searchOptions.getDateFrom()));
        } else if(searchOptions.getDateTo() != null) {
            query.put("creationDate", new BasicDBObject("$lte", searchOptions.getDateTo()+" 23:59:59"));
        }


        DBCollection coll = db.getCollection("rdlSnipData");
        DBCursor cursor = coll.find(query);

        while(cursor.hasNext()) {
            DBObject doc = cursor.next();
            SnipBean snip = buildBeanObject(doc);
            beans.add(snip);
        }
        return beans;
    }

    @Override
    public SnipBean getLastSnip(String  match) {

        List<SnipBean> beans = getAllSnips();

        SnipBean lastBean = Iterables.getLast(beans);

        return lastBean;
    }

    /**
     * crud get
     * returns a snip ===  jpa find
     * @return
     */
    @Override
    public SnipBean getSnip(String id) {
        sLogger.info("SnipServiceImpl getSnip  id: "+id);

        beanery = AutoBeanFactorySource.create(Beanery.class);
        DB db = getMongo();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCollection coll = db.getCollection("rdlSnipData");
        DBCursor cursor = coll.find(query);
        DBObject  doc =  cursor.next();

        SnipBean snip = buildBeanObject(doc);

        return snip;
    }

    /**
     * crud save
     * saves a snip ===  jpa persist
     * @param snip : Bean  to create
     * @return
     */

    @Override
    public void createSnip(SnipBean snip) {
        sLogger.info("SnipServiceImpl createSnip  title: "+snip.getTitle());
        DB db = getMongo();

        DBCollection coll = db.getCollection("rdlSnipData");
        if(snip.getId()==null)  {
            BasicDBObject doc = buildDbObject(snip);
            coll.insert(doc);
        }
    }

    /**
     * crud update
     * updates the snip
     * @param snip : Bean  to update
     * @return
     */
    @Override
    public void updateSnip(SnipBean snip) {

        sLogger.info("SnipServiceImpl updateSnip  updateSnip id: "+snip.getId());
        System.out.println("SnipServiceImpl updateSnip  updateSnip content " +snip.getId());
        System.out.println("SnipServiceImpl updateSnip  updateSnip content " +snip.getTitle());

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

    @Override
    public SnipBean incrementViewCounter(String id) {
        DB db = getMongo();
        DBCollection coll = db.getCollection("rdlSnipData");

        // build the search query
        BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(id));
        DBObject modifier = new BasicDBObject("views", 1);
        DBObject incQuery = new BasicDBObject("$inc", modifier);
        // make update
        DBObject dbObj = coll.findAndModify(searchQuery, incQuery);
        SnipBean snip = buildBeanObject(dbObj);
        return snip;
    }


    /**
     * crud delete
     * updates deletes a snip
     * @param String id   snip to delete key
     * @return
     */
    @Override
    public void deleteSnip(String id) {
        DB db = getMongo();
        DBCollection coll = db.getCollection("rdlSnipData");
        BasicDBObject deleteDocument = new BasicDBObject();
        deleteDocument.append("_id" ,new ObjectId(id));
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


    public String makeTimeStamp() {
        Date processDateTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
        String timeStampString = formatter.format(processDateTime);

        return timeStampString;
    }

    /**
     * builds the db object from Bean
     * @param : SnipBean snip
     * @return : BasicDBObject
     */
    private BasicDBObject buildDbObject(SnipBean snip) {

        BasicDBObject doc = new BasicDBObject();

        // if there is id in user instance , append it
        if(snip.getId() != null){
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

        if(snip.getLinks() == null){
            snip.setLinks(new ArrayList<SnipBean.Links>());
        }

        for (SnipBean.Links link : snip.getLinks()) {
            BasicDBObject obj = new BasicDBObject("targetId", link.getTargetId()).
                    append("rank", link.getRank());
            links.add(obj);
        }

        doc.append("links", links);

        return doc;
    }

    /**
     * builds the Bean from he db object
     * @param : DBObject doc
     * @return : SnipBean
     */
    private SnipBean buildBeanObject(DBObject doc) {

        if(beanery == null)   beanery = AutoBeanFactorySource.create(Beanery.class);
        SnipBean snip = beanery.snipBean().as();
        //new appended field  avatarUrl
        snip.setAvatarUrl((String)doc.get("avatarUrl"));
        // set the fields
        snip.setId(doc.get("_id").toString());

        snip.setRep((Integer) doc.get("rep"));
        snip.setAuthor((String) doc.get("author"));
        snip.setContent((String) doc.get("content"));
        snip.setCoreCat((String) doc.get("coreCat"));
        snip.setCreationDate((String) doc.get("creationDate"));
        snip.setEditDate((String) doc.get("editDate"));
        snip.setMoney((String) doc.get("money"));
        snip.setNegativeRef((Integer) doc.get("negativeRef"));
        snip.setNeutralRef((Integer) doc.get("neutralRef"));
        snip.setParentStream((String) doc.get("parentStream"));
        snip.setSnipType((String) doc.get("snipType"));
        snip.setViews((Integer) doc.get("views"));
        snip.setTitle((String) doc.get("title"));
        snip.setReferenceType((String) doc.get("referenceType"));
        snip.setPosRef((Integer) doc.get("posRef"));
        snip.setSubCat((String) doc.get("subCat"));
        snip.setParentTag((String) doc.get("parentTag"));
        snip.setVotes((String) doc.get("votes"));
        snip.setParentThread((String) doc.get("parentThread"));

        List<SnipBean.Links> linksList = new ArrayList<SnipBean.Links>();

        // set the List<SnipBean.Links>
        BasicDBList links = (BasicDBList)doc.get("links");

        for(Object obj : links){
            SnipBean.Links titlesBean = beanery.snipLindsBean().as();
            titlesBean.setTargetId((String)((BasicDBObject)obj).get("targetId"));
            titlesBean.setRank((String)((BasicDBObject)obj).get("rank"));
            linksList.add(titlesBean);
        }

        snip.setLinks(linksList);

        return snip;
    }

    /**
     *  MongoClient("localhost", 27017)
     *  later the above  url will be changed to a cloud based schema hence
     *  UnknownHostException  exception
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
