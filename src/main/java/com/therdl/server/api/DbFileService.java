package com.therdl.server.api;

import java.io.File;

/**
 * this is a service for using the db<currently Mongo> as a standby file system
 */
public interface DbFileService {

    // crud for now
    void saveIamage(File imageFile);

    File getImage(String fileName);

}
