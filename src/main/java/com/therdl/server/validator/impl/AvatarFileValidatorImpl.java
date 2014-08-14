package com.therdl.server.validator.impl;

import org.apache.commons.fileupload.FileItem;

import java.util.logging.Logger;

/**
 * Contains server side logic pertaining to user's avatar file validation
 */
public class AvatarFileValidatorImpl {
	private static Logger log = Logger.getLogger(AvatarFileValidatorImpl.class.getName());

	public static boolean isImageValid (FileItem itemFile){
		log.info("AvatarFileValidation isImageValid BEGIN ");
		//TODO to implement isImageValid
		return true;
	}
}


