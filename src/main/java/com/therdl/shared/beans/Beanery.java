package com.therdl.shared.beans;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface Beanery extends AutoBeanFactory {

    AutoBean<SnipBean> snipBean();

    AutoBean<AuthUserBean> authBean();

}
