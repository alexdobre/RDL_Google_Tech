package com.therdl.shared;

import com.therdl.shared.beans.Beanery;

/**
 * This class contains java utility methods to be used throughout the project
 */

public class RDLUtils {

	private static Beanery beanery;

	/**
	 * This method is an enhanced parseInt checking for null and empty strings
	 * Please note this still throws an exception if the string is malformed
	 *
	 * @param s the object to parse
	 * @return the parsed INT, 0 if string is null or empty
	 */
	public static Integer parseInt(Object s) {
		if (s == null) return 0;
		if (s != null && s.toString().isEmpty()) ;
		return Integer.parseInt(s.toString());
	}
}
