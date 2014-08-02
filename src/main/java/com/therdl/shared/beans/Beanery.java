package com.therdl.shared.beans;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;


/**
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */
public interface Beanery extends AutoBeanFactory {

	/**
	 * main objects
	 *
	 * @return
	 */
	AutoBean<SnipBean> snipBean();

	AutoBean<UserBean> userBean();

	AutoBean<AuthUserBean> authBean();

	AutoBean<CurrentUserBean> currentUserBean();

	/**
	 * nested objects
	 *
	 * @return
	 */
	AutoBean<SnipBean.Link> snipLinksBean();

	AutoBean<UserBean.VotesGivenBean> userVotesGivenBean();

	AutoBean<UserBean.RepGivenBean> userRepGivenBean();

	AutoBean<UserBean.RefGivenBean> userRefGivenBean();

	AutoBean<UserBean.FriendBean> userFriendBean();

	AutoBean<UserBean.TitleBean> userTitleBean();

	AutoBean<UserBean.MessageBean> userMessageBean();
}
