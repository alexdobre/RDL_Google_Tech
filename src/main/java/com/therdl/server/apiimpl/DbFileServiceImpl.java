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
 *  @ String defaultDatabaseName, mongo database, in this case is 'rdl'
 *  @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */
public class DbFileServiceImpl implements DbFileService {

    private String defaultDatabaseName;
    private Beanery beanery;

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(DbFileServiceImpl.class);

    /**
     * persist an image as a file
     * @param File imageFile the file to be saved
     * @param String fileName the file name
     */
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

    /**
     * persist an image as a ByteArray
     * @param byte[] imageBtyeArray  a byte array of image data
     * @param String fileName
     */
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

    /**
     * crud get
     * @param fileName  name of file to retrieve
     * @return
     */
    @Override
    public File getImage(String fileName) {
        DB db = getMongo();
        GridFS gfsAvatarImage = new GridFS(db, "avatar");
        GridFSDBFile savedAvatarImage= gfsAvatarImage.findOne(fileName);
        // here we need to write the file to the filesystem so it can be picked up by javascript

        return null;
    }

    /**
     *  crud update
     * @param String avatarDirUrl his uri is used for the javascript layer
     * to retrieve the image
     * @param String fileName name of the image
     * @return
     */
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
