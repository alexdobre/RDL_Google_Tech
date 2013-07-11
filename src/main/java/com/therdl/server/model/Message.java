package com.therdl.server.model;

import java.util.ArrayList;



/**
 * A private message between users
 * @author Alex
 *
 */

public class Message extends Snip {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<UserProfile> destList;

	public void setDestList(ArrayList<UserProfile> destList) {
		this.destList = destList;
	}

	public ArrayList<UserProfile> getDestList() {
		return destList;
	}
	
}
