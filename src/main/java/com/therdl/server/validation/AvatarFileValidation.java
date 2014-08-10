package com.therdl.server.validation;

import org.apache.commons.fileupload.FileItem;

import java.util.logging.Logger;

/**
 * Contains server side logic pertaining to user's avatar file validation
 */
public class AvatarFileValidation {
	private static Logger log = Logger.getLogger(AvatarFileValidation.class.getName());

	public static boolean isImageValid (FileItem itemFile){
		log.info("AvatarFileValidation isImageValid BEGIN ");
		//TODO to implement isImageValid
		return true;
	}
}


