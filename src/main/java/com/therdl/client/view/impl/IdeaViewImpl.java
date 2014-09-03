package com.therdl.client.view.impl;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.IdeaView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The ideas view (Snip, Material, FastCap, Habit)
 */
public class IdeaViewImpl extends SnipViewImpl implements IdeaView{

	public IdeaViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(currentUserBean, appMenu);
	}
}
