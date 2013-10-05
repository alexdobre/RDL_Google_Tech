package com.therdl.server.data;

import com.therdl.server.api.DbFileService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 *  as LocalFileStorge only uses Mongo for cross build
 *  persistance
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

    @Override
    public String storeFile(FileData file, String fileExtension) {
     // not implemented

        return null;
    }

    @Override
    public String storeFileDb(FileData file, String fileName) {

        dbFileService.saveImageBytes(file.getBytes(), fileName);
        return "0k";
    }

    @Override
    public boolean setAvatarForUserFromDb(String avatarDirUrl, String fileName) {

     return   dbFileService.setUserAvatar(avatarDirUrl,fileName );

    }


}
