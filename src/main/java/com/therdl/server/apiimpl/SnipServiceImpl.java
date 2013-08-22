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



    @Override
    public List<SnipBean> getAllSnips(String match) {
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
            SnipBean snip = beanery.snipBean().as();
            snip.setId((String)doc.get("_id").toString());
            snip.setServerMessage((String)doc.get("serverMessage"));
            snip.setStream((String) doc.get("stream"));
            snip.setTimeStamp((String) doc.get("timeStamp"));
            snip.setContentAsString((String) doc.get("contentAsString"));
            snip.setContentAsHtml((String) doc.get("contentAsHtml"));
            snip.setAuthor((String) doc.get("author"));
            snip.setTitle((String) doc.get("title"));
            beans.add(snip);
        }

        return beans;

    }


    @Override
    public SnipBean getLastSnip(String  match) {

        List<SnipBean> beans = getAllSnips( match);

        SnipBean lastBean = Iterables.getLast(beans);

        return lastBean;
    }




    @Override
    public SnipBean getSnip(String id) {
        sLogger.info("SnipServiceImpl getSnip  id: "+id);

        beanery = AutoBeanFactorySource.create(Beanery.class);
        SnipBean snip = beanery.snipBean().as();
        DB db = getMongo();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCollection coll = db.getCollection("rdlSnipData");
        DBCursor cursor = coll.find(query);
        DBObject  doc=  cursor.next();
        snip.setId((String)doc.get("_id").toString());
        snip.setServerMessage((String)doc.get("serverMessage"));
        snip.setStream((String) doc.get("stream"));
        snip.setTimeStamp((String) doc.get("timeStamp"));
        snip.setContentAsString((String) doc.get("contentAsString"));
        snip.setContentAsHtml((String) doc.get("contentAsHtml"));
        snip.setAuthor((String) doc.get("author"));
        snip.setTitle((String) doc.get("title"));
        snip.setAction((String) doc.get("action"));

        return snip;
    }

    @Override
    public void createSnip(SnipBean snip) {
        sLogger.info("SnipServiceImpl createSnip  title: "+snip.getTitle());
        DB db = getMongo();

        DBCollection coll = db.getCollection("rdlSnipData");
        if(snip.getId()==null)  {
        BasicDBObject doc = new BasicDBObject("user", "testRdlUser").
                append("title", snip.getTitle()).
                append("contentAsString", snip.getContentAsString()).
                append("contentAsHtml", snip.getContentAsHtml()).
                append("author", snip.getAuthor()).
                append("timeStamp",makeTimeStamp()).
                append("stream", snip.getStream()).
                append("action",snip.getAction()).
                append("serverMessage", snip.getServerMessage());


        coll.insert(doc);

        }


    }


    @Override
    public void updateSnip(SnipBean snip) {
        sLogger.info("SnipServiceImpl updateSnip  updateSnip id: "+snip.getId());
        DB db = getMongo();
        DBCollection coll = db.getCollection("rdlSnipData");

        System.out.println("SnipServiceImpl updateSnip  updateSnip content " +snip.getContentAsString());
        System.out.println("SnipServiceImpl updateSnip  updateSnip content " +snip.getTitle());
        System.out.println("SnipServiceImpl updateSnip  updateSnip content " +snip.getId());

        BasicDBObject updateThis = new BasicDBObject();

        updateThis.put("_id", new ObjectId(snip.getId()));

        DBObject newObject =  coll.find(updateThis).toArray().get(0);

        newObject.put("contentAsHtml",snip.getContentAsHtml());
        newObject.put("title", snip.getTitle());
        newObject.put("contentAsString",snip.getContentAsString());

        coll.findAndModify(updateThis, newObject);

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
