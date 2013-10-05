package com.therdl.server.data;

/**
 */
public interface FileStorage {

    /**
     * Get a storage URL for the file identified with the following name.
     * @param fileName the relative file name
     * @return the full path to the file in the store
     */
    String absoluteUrl(String fileName);



    /**
     * Put a file into storage.
     *
     * @param file the file data
     * @param fileExtension
     * @return a URL that can be used to obtain the file
     */
    String storeFile(FileData file, String fileExtension);

    String storeFileDb(FileData file, String fileName);

    boolean  setAvatarForUserFromDb(String avatarDirUrl, String fileName);

}
