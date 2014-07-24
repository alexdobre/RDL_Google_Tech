package com.therdl.server.util;

import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Contains business logic common utility methods to be used on the server side
 * Created by Alex on 03/02/14.
 */
public class ServerUtils {

	private static Logger log = Logger.getLogger(EmailSender.class.getName());

	/**
	 * Encrypts a string - used for storing passwords
	 *
	 * @param input the string to be encrypted
	 * @return the encrypted string
	 */
	public static String encryptString(String input) {
		if (input == null || input.equals("")) {
			return null;
		}
		String hash = BCrypt.hashpw(input, BCrypt.gensalt());
		return hash;
	}

	/**
	 * Generates a new password for the user
	 *
	 * @return the randomly generated password
	 */
	public static String generatePassword() {
		//we generate a random sequence of 6 numbers
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append((int) (Math.random() * 10));
		}

		return sb.toString();
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
