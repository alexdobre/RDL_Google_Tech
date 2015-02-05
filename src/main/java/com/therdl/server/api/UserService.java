package com.therdl.server.api;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.UserBean;


/**
 * this is a service for User crud operations
 */
public interface UserService {

	/**
	 * gets the user Bean
	 *
	 * @param username
	 * @return
	 */
	UserBean getUserByUsername(String username);

	/**
	 * gets the user Bean
	 *
	 * @param email
	 * @return UserBean
	 */
	UserBean getUserByEmail(String email);

	/**
	 * gets the user Bean
	 *
	 * @param id
	 * @return UserBean
	 */
	UserBean getUserById(String id);

	/**
	 * gets the user Bean
	 *
	 * @param paypalId
	 * @return UserBean
	 */
	UserBean getUserByPayPalId(String paypalId);

	/**
	 * creates the new user
	 *
	 * @param user
	 */
	void createUser(UserBean user);


	/**
	 * Changes the pass
	 * @param userBean the existing user bean
	 * @param pass the new pass
	 */
	void changePass(UserBean userBean, String pass);

	/**
	 * user json contains a list of reference given objects, which stores the snip ids and date that user wrote a reference
	 * the function adds a reference given object to the list. Finds user by email.
	 *
	 * @param refGivenBean refGivenBean
	 * @param userEmail    email
	 * @return modified UserBean
	 */
	public UserBean addRefGiven(AutoBean<UserBean.RefGivenBean> refGivenBean, String userEmail);

	/**
	 * checks if user wrote a reference to the snip with the given snipId
	 *
	 * @param viewerUsername  user username
	 * @param snipId snipId
	 * @return Integer 1 or 0
	 */
	public Integer isRefGivenForSnip(String viewerUsername, String snipId);

	/**
	 * updates the user
	 *
	 * @param user
	 * @return
	 */
	public void updateUser(UserBean user);

	/**
	 * finds a user on email
	 *
	 * @param bean
	 * @param hash
	 * @return UserBean
	 */
	public AutoBean<AuthUserBean> authUser(AuthUserBean bean, String hash);

	/**
	 * Finds a user by SID
	 *
	 * @param sid
	 * @return
	 */
	public AutoBean<AuthUserBean> findUserBySid(String sid);

	/**
	 * Checks if a user is an RDL supporter
	 * @param username
	 * @return
	 */
	public Boolean isSupporter (String username);

	/**
	 * Increments a counter on the user
	 * @param username the username
	 * @param field field to increment
	 */
	public void incrementCounter(String username, String field);

	/**
	 * block the user and notify
	 * @param email
	 */
	public void blockUser(String email,String option);
}
