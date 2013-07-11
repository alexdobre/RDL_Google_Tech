package com.therdl.server.model;

/**
 * User information inside TheRDL
 * @author Alex
 *
 */
import java.io.Serializable;
import java.util.ArrayList;



public class UserProfile implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private  Long id;
	
	private  String alias;
	
	private  String realId;
	
	/**
	 * Groups where the user is moderator
	 */
	private ArrayList<Group> groupsModerator;
	
	/**
	 * Group where the user is member
	 */
	private ArrayList<Group> groupsMember;
	
	private ReputationLevel reputationLevel;
	
	private Integer likes;
	
	private Integer dislikes;
	
	private ArrayList<UserProfile> vouches;
	
	private ArrayList<UserProfile> friends;
	
	/**
	 * Forum posts signature
	 */
	private String signature;
	
	/**
	   * The RequestFactory requires an Integer version property for each proxied
	   * type, but makes no good use of it. This requirement will be removed soon.
	   */
	private  Integer version = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setRealId(String realId) {
		this.realId = realId;
	}

	public String getRealId() {
		return realId;
	}

	public void setGroupsModerator(ArrayList<Group> groupsModerator) {
		this.groupsModerator = groupsModerator;
	}

	public ArrayList<Group> getGroupsModerator() {
		return groupsModerator;
	}

	public void setGroupsMember(ArrayList<Group> groupsMember) {
		this.groupsMember = groupsMember;
	}

	public ArrayList<Group> getGroupsMember() {
		return groupsMember;
	}

	public ReputationLevel getReputationLevel() {
		return reputationLevel;
	}

	public void setReputationLevel(ReputationLevel reputationLevel) {
		this.reputationLevel = reputationLevel;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public ArrayList<UserProfile> getVouches() {
		return vouches;
	}

	public void setVouches(ArrayList<UserProfile> vouches) {
		this.vouches = vouches;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	public void setFriends(ArrayList<UserProfile> friends) {
		this.friends = friends;
	}

	public ArrayList<UserProfile> getFriends() {
		return friends;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}
	
}
