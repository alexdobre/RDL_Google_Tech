package com.therdl.server.apiimpl;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.therdl.server.api.DbFileService;
import com.therdl.shared.beans.Beanery;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.UnknownHostException;

/**
 *  A file service implementation for images
 *  uses Mongo libary GridFS can be extended to
 *  video and audio
 */
public class DbFileServiceImpl implements DbFileService {

    private String defaultDatabaseName;
    private Beanery beanery;

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(DbFileServiceImpl.class);


    @Override
    public void saveIamage(File imageFile) {
        DB db = getMongo();
        GridFS gfsPhoto = new GridFS(db, "avatar");

    }

    @Override
    public File getImage(String fileName) {

        return null;
    }




    // later the url will be a cloud based schema hence exception
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
