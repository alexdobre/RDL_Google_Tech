package com.therdl.server.data;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Filesystem  storage implementation of the  FileStorage type
 *
 * @ String storageUrl, uri base for eventual file uri
 * @ File storageDirectory , directory object for stored files
 * @ boolean deleteOnExit, clean up for temp files
 */
public class LocalFileStorage implements FileStorage {

	private final String storageUrl;

	// this file object is used only to create the local storage directory if it does not exist
	private final File storageDirectory;

	private boolean deleteOnExit;

	public LocalFileStorage(String storageUrl) {
		this.storageUrl = storageUrl;
		storageDirectory = new File(storageUrl);

	}

	public void setDeleteOnExit(boolean deleteOnExit) {
		this.deleteOnExit = deleteOnExit;
	}

	public String absoluteUrl(String fileName) {
		return storageUrl + fileName;
	}

	/**
	 * crud save === jpa persist
	 *
	 * @param FileData fileData, the file bytes store
	 * @param String   fileExtension, currently only jpeg supported but this will
	 *                 be extended
	 * @return
	 */
	public String storeFile(FileData fileData, String fileExtension) {

		String resutlt = "initialising file save opperation for  " + fileData.getName() + " : " + fileExtension;
		System.out.println("storageDirectory.getName(): " + storageDirectory.getName());
		System.out.println("resutlt: " + resutlt);
		storageDirectory.setExecutable(true);
		storageDirectory.setReadable(true);
		storageDirectory.setWritable(true);

		System.out.println("Is Execute allow : " + storageDirectory.canExecute());
		System.out.println("Is Write allow : " + storageDirectory.canWrite());
		System.out.println("Is Read allow : " + storageDirectory.canRead());

		if (!storageDirectory.exists()) {
			System.out.println("LocalFileStorage storageDirectory does not exist ");
			if (storageDirectory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}

		// basically  at this point we are past a huge null pointer check for local file storage exists
		// hopefully we have ensured file directory exists and we have permissions
		// now construct file and write the image data to the file
		System.out.println("LocalFileStorage fileUrl " + fileData.getName());
		System.out.println("LocalFileStorage fileUrl " + fileExtension);
		// for now only handle jpeg images
		String fileUrl = storageUrl + File.separator + fileData.getName() + "small.jpg";
		System.out.println("LocalFileStorage fileUrl");

		File file = new File(fileUrl);
		System.out.println("LocalFileStorage file = new File(fileUrl);" + file.getAbsolutePath());
		try {
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file);
			os.write(fileData.getBytes());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		resutlt = file.toURI().toString();


		return resutlt;
	}


	/**
	 * not supported in filesystem storage
	 *
	 * @param file
	 * @param fileName
	 * @return
	 */
	@Override
	public String storeFileDb(FileData file, String fileName) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * sets the uri for the image
	 * not supported in filesystem storage, as uri is included in file object
	 *
	 * @param avatarDirUrl
	 * @param fileName
	 * @return
	 */
	@Override
	public boolean setAvatarForUserFromDb(String avatarDirUrl, String fileName) {
		//To change body of implemented methods use File | Settings | File Templates.
		return false;
	}
}
