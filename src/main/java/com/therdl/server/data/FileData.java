package com.therdl.server.data;

/**
 *
 */
public class FileData {

    private final String name;

    private final byte[] bytes;

    private final String contentType;

    public FileData(String name, byte[] bytes, String contentType) {
        this.name = name;
        this.bytes = bytes;
        this.contentType = contentType;
    }

    /**
     * The name of the file.
     */
    public String getName() {
        return name;
    }

    /**
     * The file data as a byte array.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * The file content type.
     */
    public String getContentType() {
        return contentType;
    }



}
