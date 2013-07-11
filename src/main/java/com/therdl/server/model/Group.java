package com.therdl.server.model;


/**
 * Groups where users can freely discuss relationship issues
 * @author Alex
 *
 */

public class Group extends Snip {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The admin is the creator of the group and has power user rights on it 
	 */
	private UserProfile admin;
		
	
	/**
	 * If the admin chooses access to the group will be password protected
	 */
	private String password;
	
	/**
	 * A group may be marked as private to not be searchable and it's posts inaccessible 
	 * for non members
	 */
	private Boolean isPrivate;
	
	
	/**
	 * This marks a group as invitation or password only but it's posts are searchable
	 * and viewable by non members. Only members can post.
	 */
	private Boolean viewOnly;


	public UserProfile getAdmin() {
		return admin;
	}


	public void setAdmin(UserProfile admin) {
		this.admin = admin;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Boolean getIsPrivate() {
		return isPrivate;
	}


	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}


	public Boolean getViewOnly() {
		return viewOnly;
	}


	public void setViewOnly(Boolean viewOnly) {
		this.viewOnly = viewOnly;
	}
}
