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

    private final String storageUrl;

    private final File storageDirectory;

    private boolean deleteOnExit;

    /**
     * Constructs a local file storage.
     * @param storageUrl the logical URL pointing to the root of this file storage
     * @param storageDirectory the path to the base directory on the filesystem where files physically reside; created if necessary.
     */
    public LocalFileStorage(String storageUrl, File storageDirectory) {
        this.storageUrl = storageUrl;
        try {
            this.storageDirectory = storageDirectory;
            this.storageDirectory.deleteOnExit();
            this.storageDirectory.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Whether the files stored in local storage should be automatically deleted when the VM shuts down.
     * Useful for temporary file stores.  Defaults to false.
     */
    public void setDeleteOnExit(boolean deleteOnExit) {
        this.deleteOnExit = deleteOnExit;
    }

    public String absoluteUrl(String fileName) {
        return storageUrl + fileName;
    }

    public String storeFile(FileData fileData) {
        File file = new File(storageDirectory, fileData.getName());
        if (file.exists()) {
            file.delete();
        }
        File parent = file.getParentFile();
        if (parent != null && !parent.equals(storageDirectory)) {
            parent.mkdirs();
        }
        LinkedList<File> parents = new LinkedList<File>();
        while (parent != null && !parent.equals(storageDirectory)) {
            parents.addFirst(parent);
            parent = parent.getParentFile();
        }
        if (deleteOnExit) {
            for (File p : parents) {
                p.deleteOnExit();
            }
            file.deleteOnExit();
        }
        try {
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            os.write(fileData.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file.toURI().toString();
    }
}
