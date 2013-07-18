package com.therdl.server.apiimpl;


import com.google.common.collect.Iterables;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.mongodb.*;
import com.therdl.server.api.SnipsService;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.slf4j.LoggerFactory;


import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;


@Singleton
public class SnipServiceImpl implements SnipsService {

    private MongoURI uri;
    private String defaultDatabaseName;
    private Properties configProp;
    Beanery beanery;

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
        return null;
    }

    @Override
    public void createSnip(SnipBean snip) {

        DB db = getMongo();

        DBCollection coll = db.getCollection("rdlSnipData");
        if(snip.getId()==null)  {
        BasicDBObject doc = new BasicDBObject("user", "testRdlUser").
                append("title", snip.getTitle()).
                append("contentAsString", snip.getContentAsString()).
                append("contentAsHtml", snip.getContentAsHtml()).
                append("author", snip.getAuthor()).
                append("timeStamp",snip.getTimeStamp()).
                append("stream", snip.getStream()).
                append("action",snip.getAction()).
                append("serverMessage", snip.getServerMessage());


        coll.insert(doc);

        }


    }

    @Override
    public void deleteSnip(String id) {

    }

    @Override
    public SnipBean updateSnip(SnipBean snip) {
        return null;

    }

    @Override
    public String getDebugString() {
        return "Snip Service wired up for guice injection ok";
    }




    // later the url will be a cloud based schema hence exception
    private DB getMongo() {
        configProp = new Properties();
        try {
            configProp.load(new FileInputStream("src/main/resources/config.properties"));
        }  catch (IOException e) { e.printStackTrace();  }
        defaultDatabaseName = configProp.getProperty("mongodb.default.database");
        sLogger.info("CreateAppContext from properties  default.database  :  " + defaultDatabaseName);


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
