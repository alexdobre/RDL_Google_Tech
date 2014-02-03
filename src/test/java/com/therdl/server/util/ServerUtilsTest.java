package com.therdl.server.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Tests the ServerUtils class
 * Created by Alex on 03/02/14.
 */
public class ServerUtilsTest {

    @Test
    public void testGeneratePassword() {
        System.out.println("testGeneratePassword BEGIN");
        String pass = ServerUtils.generatePassword();
        System.out.println("pass: "+pass);

        assertEquals(6, pass.length()); 
        for (char c: pass.toCharArray()){
            assertTrue(Character.isDigit(c));
        }
        System.out.println("testGeneratePassword END");
    }

}
