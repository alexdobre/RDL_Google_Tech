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


@Singleton
public class SnipServiceImpl implements SnipsService {

    private MongoURI uri;
    private String defaultDatabaseName;
    private Properties configProp;
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

        DBCursor collDocs  =  coll.find();

        while(collDocs.hasNext()) {
            DBObject doc = collDocs.next();
            SnipBean snip = buildBeanObject(doc);
            beans.add(snip);
        }

        return beans;
    }

    @Override
    public List<SnipBean> getSnipsWith(String title) {
        DB db = getMongo();
        List<SnipBean> beans = new ArrayList<SnipBean>();
        BasicDBObject query = new BasicDBObject();
        query.put("title", title);
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
     * updates the object in the db
     * @param snip : Bean object to update with
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


    private String makeTimeStamp() {


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
        SnipBean snip = beanery.snipBean().as();

        // set the fields
        snip.setId(doc.get("_id").toString());
        snip.setRep((String) doc.get("rep"));
        snip.setAuthor((String) doc.get("author"));
        snip.setContent((String) doc.get("content"));
        snip.setCoreCat((String) doc.get("coreCat"));
        snip.setCreationDate((String) doc.get("creationDate"));
        snip.setEditDate((String) doc.get("editDate"));
        snip.setMoney((String) doc.get("money"));
        snip.setNegativeRef((String) doc.get("negativeRef"));
        snip.setNeutralRef((String) doc.get("neutralRef"));
        snip.setParentStream((String) doc.get("parentStream"));
        snip.setSnipType((String) doc.get("snipType"));
        snip.setViews((String) doc.get("views"));
        snip.setTitle((String) doc.get("title"));
        snip.setReferenceType((String) doc.get("referenceType"));
        snip.setPosRef((String) doc.get("posRef"));
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

    // later the url will be a cloud based schema hence exception
    private DB getMongo() {
        configProp = new Properties();
        try {
            configProp.load(new FileInputStream("src/main/resources/config.properties"));
        }  catch (IOException e) { e.printStackTrace();  }
        defaultDatabaseName = configProp.getProperty("mongodb.default.database");
        sLogger.info("CreateAppContext from properties  default.database  :  " + defaultDatabaseName);
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
