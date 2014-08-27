package com.therdl.server.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Tests the ServerUtils class
 */
public class ServerUtilsTest {

	@Test
	public void testGeneratePassword() {
		System.out.println("testGeneratePassword BEGIN");
		String pass = ServerUtils.generatePassword();
		System.out.println("pass: " + pass);

		assertEquals(6, pass.length());
		for (char c : pass.toCharArray()) {
			assertTrue(Character.isDigit(c));
		}
		System.out.println("testGeneratePassword END");
	}

}
