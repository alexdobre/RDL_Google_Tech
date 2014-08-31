package com.therdl.shared.beans;

/**
 * Encapsulates user reputation actions
 */
public interface RepBean extends TokenizedBean {

	String getId();

	void setId(String id);

	String getSnipId();

	void setSnipId(String snipId);

	String getUserId();

	void setUserId(String userId);

	String getDateCreated();

	void setDateCreated(String dateCreated);
}
