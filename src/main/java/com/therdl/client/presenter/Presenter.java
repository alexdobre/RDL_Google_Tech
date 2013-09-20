package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;

public abstract interface Presenter {
	public abstract void go(final HasWidgets container);
    void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean);
}


