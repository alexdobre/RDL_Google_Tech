package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;


/**
 * Presenter superclass interface for the Presenter in the
 * Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @  void go(final HasWidgets container,  AutoBean<CurrentUserBean> currentUserBean)
 *  HasWidgets container A widget that implements this type contains widgets and can enumerate them
 *  AutoBean<CurrentUserBean> currentUserBean see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 *  this bean maintains client side user state information
 *
 */
public abstract interface Presenter {

	public abstract void go(final HasWidgets container);

    void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean);
}


