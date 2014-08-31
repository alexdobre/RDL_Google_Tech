package com.therdl.shared.beans;

import java.util.List;


/**
 * A UserBean, in the
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * for new developers important to understand GWT AutoBean client/server architecture
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanFactory
 * the AutoBean design pattern allows us to use the same beans in the java layer on
 * the server and the javascript layer in the browser(recall GWT java code runs as javascript)
 * UserBean is used on the client as a java class for the Mongo persistence layer
 */
public interface UserBean extends TokenizedBean {

	/**
	 * used for implementing the command pattern in this application
	 * for actions see
	 * http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
	 *
	 * @return
	 */
	String getAction();

	void setAction(String action);

	/**************************** Getters and Setters ******************************/

	/**
	 * the unique id, primary key
	 */
	String getId();

	void setId(String id);

	/**
	 * methods below are for standard form based credentials submitted on the clien
	 * for user login and sign up
	 *
	 * @return
	 */
	String getUsername();

	void setUsername(String username);

	String getPassHash();

	void setPassHash(String passHash);

	String getEmail();

	void setEmail(String email);

	String getSid();

	void setSid(String sid);

	String getPaypalId();

	void setPaypalId(String paypalId);

	/**
	 * methods below are composition for the Snip rdl schema
	 */

	Integer getRep();

	void setRep(Integer rep);

	List<TitleBean> getTitles();

	void setTitles(List<TitleBean> titles);

	List<FriendBean> getFriends();

	void setFriends(List<FriendBean> friends);

	/**
	 * nested interfaces
	 */
	interface TitleBean {
		public String getTitleName();

		public void setTitleName(String titleName);

		public String getDateGained();

		public void setDateGained(String dateGained);

		public String getExpires();

		public void setExpires(String expires);
	}

	interface FriendBean {
		public String getUsername();

		public void setUsername(String username);

		public List<MessageBean> getMessages();

		public void setMessages(List<MessageBean> messages);
	}

	interface RepGivenBean {
		public String getSnipId();

		public void setSnipId(String snipId);

		public String getDate();

		public void setDate(String date);
	}

	interface RefGivenBean {
		public String getSnipId();

		public void setSnipId(String snipId);

		public String getDate();

		public void setDate(String date);
	}

	interface VotesGivenBean {
		public String getProposalId();

		public void setProposalId(String proposalId);

		public String getDate();

		public void setDate(String date);
	}

	interface MessageBean {
		public String getMessageId();

		public void setMessageId(String messageId);

		public String getDate();

		public void setDate(String date);
	}
}
