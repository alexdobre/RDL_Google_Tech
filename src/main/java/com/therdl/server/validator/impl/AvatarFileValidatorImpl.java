package com.therdl.server.validator.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains server side logic pertaining to user's avatar file validation
 */
public class AvatarFileValidatorImpl {
	static final Logger log = LoggerFactory.getLogger(AvatarFileValidatorImpl.class);

	public static boolean isImageValid(FileItem itemFile) throws IOException{
		log.info("AvatarFileValidation isImageValid BEGIN ");
		//only jpeg
		if (!itemFile.getContentType().equals("image/jpeg")){
			return false;
		}
		if (itemFile.getSize() > 1024 * 50){
			return false;
		}

		//check size
		BufferedInputStream bis = new BufferedInputStream(itemFile.getInputStream());

		//BufferedImage
		BufferedImage bufimg = ImageIO.read(bis);

		//check size of image
		int img_width = bufimg.getWidth();
		int img_height = bufimg.getHeight();
		log.info("Checking image size: "+img_height+" / "+img_width);
		if (img_height > 100 || img_width > 100){
			return false;
		}

		return true;
	}
}


