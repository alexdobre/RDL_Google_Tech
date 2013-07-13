package com.therdl.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Central model class forms the base of most of the RDL information
 * @author Alex
 *
 */

public class Snip implements Serializable{
	
	private static final long serialVersionUID = 1L;


	
	private  String title;
	
	private String content;
	
	private  String author;
		
	/**
	 * Any Snip can be authored anonymously using the user's alias
	 */
	private String isAlias;
	
	/**
	 * this is the date of creation
	 */
	private String date;

	
	/**
	 * Service methods delegating to the service implementation
	 * @return
	 */
	//TODO move this with locators once you figure out how 
	// see SnipRequest

	public static Snip save(Snip snip) {

		return snip;
	}




	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}	

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setIsAlias(String isAlias) {
		this.isAlias = isAlias;
	}

	public String getIsAlias() {
		return isAlias;
	}


	
}
