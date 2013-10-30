package com.therdl.server.api;

import java.io.File;

/**
 * this is a service for using the db<currently Mongo> as a standby file system
 * see  com.therdl.server.apiimpl.DbFileServiceImpl
 *
 * @ saveIamage(File imageFile, String fileName) crud save
 * @ saveImageBytes(byte[] imageBtyeArray, String fileName) crud save for a byte array
 * @ File getImage(String fileName) crud read
 * @ boolean setUserAvatar(String avatarDirUrl, String fileName) crud create
 */
public interface DbFileService {

    void saveIamage(File imageFile, String fileName);

    void saveImageBytes(byte[] imageBtyeArray, String fileName);

    File getImage(String fileName);

    boolean setUserAvatar(String avatarDirUrl, String fileName);


}
