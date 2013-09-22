package com.therdl.server.data;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 */
public class LocalFileStorage  implements FileStorage  {

    private  String storageUrl;

    private final File storageDirectory;

    private boolean deleteOnExit;

    public LocalFileStorage(String storageUrl) {
        storageUrl = storageUrl;
        storageDirectory = new  File(storageUrl);

    }

    public void setDeleteOnExit(boolean deleteOnExit) {
        this.deleteOnExit = deleteOnExit;
    }

    public String absoluteUrl(String fileName) {
        return storageUrl + fileName;
    }

    public String storeFile(FileData fileData) {

        storageDirectory.setExecutable(true);
        storageDirectory.setReadable(true);
        storageDirectory.setWritable(true);

        System.out.println("Is Execute allow : " + storageDirectory.canExecute());
        System.out.println("Is Write allow : " + storageDirectory.canWrite());
        System.out.println("Is Read allow : " + storageDirectory.canRead());

        if (!storageDirectory.exists()) {

            if (storageDirectory.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }

        }

        return storageDirectory.toURI().toString();
    }
}
