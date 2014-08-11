package com.therdl.shared.beans;

import java.util.List;

/**
 * See AuthUserBean javadoc for use
 * use this bean only to maintain client side state
 * temp solution until Internet Explorer is discontinued or improved
 * once IE7 is no longer used move to HTML5 session for success
 */
public interface CurrentUserBean {

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
	 * this method is used to encapsulate the users authorisation status
	 * after a registration event
	 * goal is to minimise password transport
	 *
	 * @return
	 */
	boolean isRegistered();

	/**
	 * this method is used to encapsulate the users authorisation status
	 * goal is to minimise password transport
	 * boolean b set after sign up, true is signn up success
	 *
	 * @return
	 */
	void setRegistered(boolean b);

	List<UserBean.TitleBean> getTitles();

	void setTitles(List<UserBean.TitleBean> titles);

	boolean getIsRDLSupporter();

	void setIsRDLSupporter(boolean isRDLSupporter);

}
