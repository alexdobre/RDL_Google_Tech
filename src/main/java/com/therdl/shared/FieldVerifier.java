package com.therdl.shared;

import java.util.logging.Logger;

/**
 * Stub class for form field and any other type of user input
 */
public class FieldVerifier {

    private static Logger log = Logger.getLogger("");


    public static boolean isValidName(String name) {
        if (name == null) {

            log.info("FieldVerifier isValidName name == null");
            return false;
        }

        log.info("FieldVerifier isValidName name " +name);
        return true;
    }


    public static boolean confirmPassword(String psswd1 , String psswd2) {

        if(psswd1.equals(psswd2))return true;
        else return false;

    }


}
