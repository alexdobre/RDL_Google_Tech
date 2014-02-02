package com.therdl.shared;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;

public interface LoginHandler {
    public void onSuccess(AutoBean<CurrentUserBean> currentUserBean);
}
