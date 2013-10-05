package com.therdl.server.apiimpl;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.therdl.server.api.DbFileService;
import com.therdl.shared.beans.Beanery;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
    public void saveIamage(File imageFile, String fileName) {
        DB db = getMongo();
        // create a file store
        GridFS gfsAvatarImage = new GridFS(db, "avatar");
        // create a gfs file object
        try {
            GridFSInputFile gfsFile = gfsAvatarImage.createFile(imageFile);
            // name the file
            gfsFile.setFilename(fileName);
            // save the file
            gfsFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void saveImageBytes(byte[] imageBtyeArray, String fileName) {

        DB db = getMongo();
        // create a file store
        GridFS gfsAvatarImage = new GridFS(db, "avatar");
        // create a gfs file object from a byte array
        GridFSInputFile gfsFile = gfsAvatarImage.createFile(imageBtyeArray);
        // name the file
        gfsFile.setFilename(fileName);
        // save the file
        gfsFile.save();
    }


    @Override
    public File getImage(String fileName) {
        DB db = getMongo();
        GridFS gfsAvatarImage = new GridFS(db, "avatar");
        GridFSDBFile savedAvatarImage= gfsAvatarImage.findOne(fileName);
        // here we need to write the file to the filesystem so it can be picked up by javascript

        return null;
    }

    @Override
    public boolean setUserAvatar(String avatarDirUrl, String fileName) {

        DB db = getMongo();
        GridFS gfsAvatarImage = new GridFS(db, "avatar");
        GridFSDBFile savedAvatarImage= gfsAvatarImage.findOne(fileName);
        if (savedAvatarImage != null) {

            File avatarFile = new File(avatarDirUrl+ File.separator+fileName+"small.jpg");
            try {
            avatarFile.delete();
            avatarFile.createNewFile();
            savedAvatarImage.writeTo(avatarFile);

            return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
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
