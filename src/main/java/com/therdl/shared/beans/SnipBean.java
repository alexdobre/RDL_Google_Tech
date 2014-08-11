package com.therdl.shared.beans;

import java.util.List;


/**
 * A bean that encapsulates content data
 */

public interface SnipBean {

	/**
	 * used for implementing the command pattern in this application
	 * for actions see
	 * http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
	 *
	 * @return
	 */
	String getAction();

	void setAction(String action);

	/**
	 * ************************* Getters and Setters *****************************
	 */

	String getId();

	void setId(String id);

	String getTitle();

	void setTitle(String title);

	String getContent();

	void setContent(String content);

	String getAuthor();

	void setAuthor(String author);

	String getCreationDate();

	void setCreationDate(String creationDate);

	String getDateFrom();

	void setDateFrom(String dateFrom);

	String getDateTo();

	void setDateTo(String dateTo);


	String getEditDate();

	void setEditDate(String editDate);

	String getSnipType();

	void setSnipType(String snipType);

	String getCoreCat();

	void setCoreCat(String coreCat);

	String getSubCat();

	void setSubCat(String subCat);

	Integer getViews();

	void setViews(Integer views);

	Integer getRep();

	void setRep(Integer rep);

	Integer getPosRef();

	void setPosRef(Integer posRef);

	Integer getNeutralRef();

	void setNeutralRef(Integer neutralRef);

	Integer getNegativeRef();

	void setNegativeRef(Integer negativeRef);

	String getReferenceType();

	void setReferenceType(String referenceType);

	List<Link> getLinks();

	void setLinks(List<Link> links);

	String getParentSnip();

	void setParentSnip(String parentSnip);

	String getVotes();

	void setVotes(String votes);

	String getMoney();

	void setMoney(String money);

	Integer getIsRepGivenByUser();

	void setIsRepGivenByUser(Integer repGiven);

	Integer getIsRefGivenByUser();

	void setIsRefGivenByUser(Integer refGiven);

	String getViewerId();

	void setViewerId(String id);

	int getCount();

	void setCount(int count);

	int getPageIndex();

	void setPageIndex(int pageIndex);

	String getSortField();

	void setSortField(String sortField);

	int getSortOrder();

	void setSortOrder(int sortOrder);

	Integer getPosts();

	void setPosts(Integer posts);

	Integer getPledges();

	void setPledges(Integer pledges);

	Integer getCounters();

	void setCounters(Integer counters);

	String getProposalType();

	void setProposalType(String proposalType);

	String getProposalState();

	void setProposalState(String proposalState);

	Integer getAuthorRep();

	void setAuthorRep(Integer authorRep);

	String getAuthorTitle();

	void setAuthorTitle(String authorTitle);

	/**
	 * nested interfaces
	 */
	interface Link {
		String getTargetId();

		void setTargetId(String targetId);

		String getRank();

		void setRank(String rank);
	}
}
