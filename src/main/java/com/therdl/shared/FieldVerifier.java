package com.therdl.shared;

import java.util.logging.Logger;

/**
 * Stub class for form field and any other type of user input
 * to be extended for client side validation
 */
public class FieldVerifier {

    private static Logger log = Logger.getLogger("");

    /**
     * @param name String to be checked for vaildation
     * @return boolean true for a valid field
     */
    public static boolean isValidName(String name) {
        if (name == null) {

            log.info("FieldVerifier isValidName name == null");
            return false;
        }

        log.info("FieldVerifier isValidName name " + name);
        return true;
    }

    /**
     * @param psswd1 passwords to be checked for equality
     * @param psswd2
     * @return true when passwords match
     */
    public static boolean confirmPassword(String psswd1, String psswd2) {

        if (psswd1.equals(psswd2)) return true;
        else return false;

    }


}
