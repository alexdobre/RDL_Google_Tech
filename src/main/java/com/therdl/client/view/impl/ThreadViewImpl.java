package com.therdl.client.view.impl;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ThreadView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The forum view (Thread)
 */
public class ThreadViewImpl extends SnipViewImpl implements ThreadView {
	public ThreadViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(currentUserBean, appMenu);
	}
}
