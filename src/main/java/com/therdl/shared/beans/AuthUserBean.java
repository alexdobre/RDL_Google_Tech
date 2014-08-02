package com.therdl.shared.beans;

import java.util.List;

/**
 * A bean to pass  the current authorised use;s credentials to the sever
 * for login and sign up
 * !!!! WARNING !!!!
 * this bean contains a user password
 * for client side security do not persist this bean at anytime
 * do not inject as singleton for obvious reasons
 * to maintain state use the current user bean
 * pass auth state to current user bean
 */
public interface AuthUserBean {

	/**
	 * AuthUserBean needs the avatar url to pass to currentuserbean
	 */
	String getAvatarUrl();

	/**
	 * AuthUserBean needs the avatar url to pass to currentuserbean
	 *
	 * @ String avatarUrl  the uri to locate the users image,
	 * used in the browser/javascript layer
	 */
	void setAvatarUrl(String avatarUrl);


	/**
	 * methods below are for standard form based credentials submitted on the clien
	 * for user login and sign up
	 *
	 * @return
	 */
	String getName();

	void setName(String name);

	String getEmail();

	void setEmail(String email);

	String getSid();

	void setSid(String sid);

	String getPaypalId();

	void setPaypalId(String paypalId);

	Boolean getRememberMe();

	void setRememberMe(Boolean rememberMe);

	String getPassword();

	void setPassword(String password);

	/**
	 * this method is used to encapsulate the users authorisation status
	 * goal is to minimise password transport
	 *
	 * @return
	 */
	boolean isAuth();

	/**
	 * this method is used to encapsulate the users authorisation status
	 * goal is to minimise password transport
	 * boolean auth set after login and sign up, true is logged in
	 *
	 * @return
	 */
	void setAuth(boolean auth);

	/**
	 * used for implementing the command pattern in this application
	 * for actions see
	 * http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
	 *
	 * @return
	 */
	String getAction();

	void setAction(String action);

	List<UserBean.TitleBean> getTitles();

	void setTitles(List<UserBean.TitleBean> titles);

	boolean getIsRDLSupporter();

	void setIsRDLSupporter(boolean isRDLSupporter);

	Integer getRep();

	void setRep(Integer rep);

}
