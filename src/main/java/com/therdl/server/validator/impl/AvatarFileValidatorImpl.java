package com.therdl.server.validator.impl;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains server side logic pertaining to user's avatar file validation
 */
public class AvatarFileValidatorImpl {
	static final Logger log = LoggerFactory.getLogger(AvatarFileValidatorImpl.class);

	public static boolean isImageValid(FileItem itemFile) {
		log.info("AvatarFileValidation isImageValid BEGIN ");
		//TODO to implement isImageValid
		return true;
	}
}


