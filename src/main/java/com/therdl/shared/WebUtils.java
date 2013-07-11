package com.therdl.shared;

import com.google.gwt.core.client.GWT;

public class WebUtils {
	
	private static Messages mess = (Messages) GWT.create(Messages.class);
	
	public static Messages getMessages(){
		return mess;
	}

}
