package com.therdl.shared.beans;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public interface AuthUserBean extends TokenizedBean {


	@NotEmpty
	@Pattern(regexp="^[a-zA-Z0-9_]*$")
	String getName();

	void setName(String name);

	@NotEmpty
	@Pattern(regexp="\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b")
	String getEmail();

	void setEmail(String email);

	String getSid();

	void setSid(String sid);

	String getPaypalId();

	void setPaypalId(String paypalId);

	Boolean getRememberMe();

	void setRememberMe(Boolean rememberMe);

	@NotEmpty
	@Pattern(regexp = ".{8,}")
	String getPassword();

	void setPassword(String password);

	String getPasswordConfirm();

	void setPasswordConfirm(String passwordConfirm);

	String getOldPass();

	void setOldPass(String oldPass);

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
