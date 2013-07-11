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

	private  Long id;
	
	private  String title;
	
	private String content;
	
	private  String author;
		
	/**
	 * Any Snip can be authored anonymously using the user's alias
	 */
	private Boolean isAlias;
	
	/**
	 * this is the date of creation
	 */
	private Date date;
	
	/**
	   * The RequestFactory requires an Integer version property for each proxied
	   * type, but makes no good use of it. This requirement will be removed soon.
	   */
	private  Integer version = 0;
	
	/**
	 * Service methods delegating to the service implementation
	 * @return
	 */
	//TODO move this with locators once you figure out how 
	// see SnipRequest

	public static Snip save(Snip snip) {

		return snip;
	}

	public static List<Snip> search(SnipSearch search) {
        List<Snip> snips = null;
		return snips;
	}

	public static Snip get(String id) {
        Snip snip = null;
		return snip;
	}

	public static Snip  delete(String id) {
        Snip snip = null;
        return snip;

	}
	
	public static Snip findSnip (Long id){
        Snip snip = null;
        return snip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setIsAlias(Boolean isAlias) {
		this.isAlias = isAlias;
	}

	public Boolean getIsAlias() {
		return isAlias;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}
	
}
