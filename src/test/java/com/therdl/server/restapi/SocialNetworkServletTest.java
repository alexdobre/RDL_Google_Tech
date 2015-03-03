package com.therdl.server.restapi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.AssertTrue;

import org.apache.commons.io.FileUtils;
import org.jukito.JukitoRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

import com.amazonaws.http.HttpRequest;
//import com.therdl.server.socialnetwork.SocialNetworkServlet;

@RunWith(JukitoRunner.class)
public class SocialNetworkServletTest extends Mockito {
	
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	HttpSession session = mock(HttpSession.class);	
//	@Inject SocialNetworkServlet socialNetwork;
	
	@Test
	public void test() throws ServletException, IOException {
		    when(request.getParameter("username")).thenReturn("me");
	        when(request.getParameter("password")).thenReturn("secret");
	        PrintWriter writer = new PrintWriter("somefile.txt");
	        when(response.getWriter()).thenReturn(writer);

//	        socialNetwork.doPost(request, response);

	        verify(request, atLeast(1)).getParameter("username"); // only if you want to verify username was called...
	        writer.flush(); // it may not have been flushed yet...
	        
	}

}
