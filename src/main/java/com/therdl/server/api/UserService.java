package com.therdl.server.api;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.TokenInvalidException;

import java.util.List;


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
	 * user json contains a list of reputation given objects, which stores the snip ids and date that user gave a reputation
	 * the function adds a reputation given object to the list. Finds user by email.
	 *
	 * @param repGivenBean repGivenBean
	 * @param userEmail    email
	 * @return modified UserBean
	 */
	public UserBean addRepGiven(AutoBean<UserBean.RepGivenBean> repGivenBean, String userEmail);

	/**
	 * checks if user gave a reputation to the snip with the given snipId
	 *
	 * @param email  user email
	 * @param snipId snipId
	 * @return Integer 1 or 0
	 */
	public Integer isRepGivenForSnip(String email, String snipId);

	/**
	 * sets isRepGivenByUser flag for input snip beans
	 *
	 * @param email     current user
	 * @param snipBeans snip beans as list
	 */
	public void setRepGivenForSnips(String email, List<SnipBean> snipBeans);

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
	 * @param email  user email
	 * @param snipId snipId
	 * @return Integer 1 or 0
	 */
	public Integer isRefGivenForSnip(String email, String snipId);

	/**
	 * updates the user
	 *
	 * @param user
	 * @return
	 */
	public void updateUser(UserBean user);

	/**
	 * updates the SID of the user only
	 *
	 * @param bean
	 */
	public void updateSid(AuthUserBean bean);


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
	 * Resets a user password and sends an e-mail to the user with it
	 *
	 * @param email
	 */
	public void recoverPassword(String email, String token) throws TokenInvalidException;

}
