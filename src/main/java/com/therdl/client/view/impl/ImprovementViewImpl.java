package com.therdl.client.view.impl;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ImprovementView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The improvement view
 */
public class ImprovementViewImpl extends SnipViewImpl implements ImprovementView {
	public ImprovementViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(currentUserBean, appMenu);
	}
}
