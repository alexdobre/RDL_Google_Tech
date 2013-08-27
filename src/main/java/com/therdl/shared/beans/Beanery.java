package com.therdl.shared.beans;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface Beanery extends AutoBeanFactory {

    /**
     * main objects
     * @return
     */
    AutoBean<SnipBean> snipBean();
    AutoBean<UserBean> userBean();
    AutoBean<AuthUserBean> authBean();

    /**
     * nested objects
     * @return
     */
    AutoBean<SnipBean.Links> snipLindsBean();
    AutoBean<UserBean.VotesGivenBean> userVotesGivenBean();
    AutoBean<UserBean.RepGivenBean> userRepGivenBean();
    AutoBean<UserBean.FriendBean> userFriendBean();
    AutoBean<UserBean.TitleBean> userTitleBean();
    AutoBean<UserBean.MessageBean> userMessageBean();
}
