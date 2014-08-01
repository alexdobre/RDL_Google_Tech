package com.therdl.server.data;

/**
 * there will be many types of storage for files
 * for example
 * filesystem, mongodb, Amazon s3
 * this is a common interface for each type of storage service
 */
public interface FileStorage {

	/**
	 * Get a storage URL for the file identified with the following name.
	 *
	 * @param fileName the relative file name
	 * @return the full path to the file in the store
	 */
	String absoluteUrl(String fileName);


	/**
	 * Put a file into storage.
	 *
	 * @param file          the file data
	 * @param fileExtension
	 * @return a URL that can be used to obtain the file
	 */
	String storeFile(FileData file, String fileExtension);

	/**
	 * Put a file into storage in a database.
	 *
	 * @param FileData file, byte store
	 * @param String   fileName the name of the file
	 * @return
	 */
	String storeFileDb(FileData file, String fileName);

	/**
	 * @param String avatarDirUrl, a uri to locate the avatar as a resource
	 * @param String fileName the name of the file
	 * @return
	 */
	boolean setAvatarForUserFromDb(String avatarDirUrl, String fileName);

}
