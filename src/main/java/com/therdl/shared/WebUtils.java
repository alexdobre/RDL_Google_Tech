package com.therdl.shared;

import com.google.gwt.core.client.GWT;

/**
 * Auto generated class for GWT maven plugin build
 */
public class WebUtils {
	
	private static Messages mess = (Messages) GWT.create(Messages.class);
	
	public static Messages getMessages(){
		return mess;
	}

}
