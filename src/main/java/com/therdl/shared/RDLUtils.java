package com.therdl.shared;

/**
 * Created with IntelliJ IDEA.
 * User: alx
 * Date: 11/12/13
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class contains java utility methods to be used throughout the project
 */

public class RDLUtils {

    /**
     * This method is an enhanced parseInt checking for null and empty strings
     * Please note this still throws an exception if the string is malformed
     * @param s the object to parse
     * @return the parsed INT, 0 if string is null or empty
     */
    public static Integer parseInt(Object s) {
        if ( s == null)  return 0;
        if (s!= null && s.toString().isEmpty());
        return Integer.parseInt(s.toString());
    }
}
