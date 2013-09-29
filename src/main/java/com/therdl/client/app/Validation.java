package com.therdl.client.app;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Static class for validating objects
 */
public class Validation {


    static AutoBean<CurrentUserBean> resetCurrentUserBeanFields(AutoBean<CurrentUserBean> currentUserBean) {

        currentUserBean.as().setAvatarUrl("");
        currentUserBean.as().setAuth(false);
        currentUserBean.as().setName("");
        currentUserBean.as().setEmail("");
        currentUserBean.as().setRegistered(false);
        return currentUserBean;

    }


}
