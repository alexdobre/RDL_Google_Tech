package com.therdl.server.data;

import com.therdl.server.api.DbFileService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 *  Mongo Db File Storage  implementation of the  FileStorage type
 *  see http://docs.mongodb.org/manual/core/gridfs/
 *
 *  @ HttpSession sessions, Servlet 3 api session object, use this for the current user id
 *  when constructing a unique avatar uri for this user
 *  @ DbFileService dbFileService , the file service that handles file crud
 */
public class MongoFileStorage  implements FileStorage {

    private final Provider<HttpSession> sessions;
    private DbFileService dbFileService;

    @Inject
    public MongoFileStorage(Provider<HttpSession> sessions, DbFileService fileService) {
        this.sessions = sessions;
        this.dbFileService = fileService;
    }

    @Override
    public String absoluteUrl(String fileName) {

        return "userAvatar"+ File.separator+fileName;
    }

    /**
     *  crud save === jpa persist
     * @param FileData fileData, the file bytes store
     * @param String fileExtension, currently only jpeg supported but this will
     * be extended
     * @return
     */

    @Override
    public String storeFile(FileData file, String fileExtension) {
     // not implemented

        return null;
    }

    /**
     * crud save === jpa persist
     * @param FileData file , the file bytes store
     * @param String fileExtension, currently only jpeg supported but this will
     * @return
     */

    @Override
    public String storeFileDb(FileData file, String fileName) {

        dbFileService.saveImageBytes(file.getBytes(), fileName);
        return "0k";
    }



    /**
     * sets the uri for the image
     * this uri is used by the javascript layer to retrive the user avatar image
     * @param String avatarDirUrl relative uri to the image
     * @param String fileName file name of image
     * @return
     */
    @Override
    public boolean setAvatarForUserFromDb(String avatarDirUrl, String fileName) {

     return   dbFileService.setUserAvatar(avatarDirUrl,fileName );

    }


}
