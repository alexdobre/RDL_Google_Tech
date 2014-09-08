package com.therdl.client.view.impl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.ThreadView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The forum view (Thread)
 */
public class ThreadViewImpl extends SnipViewImpl implements ThreadView {
	public ThreadViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(currentUserBean, appMenu);

		btnTextShow = RDL.i18n.showPosts();
		btnTextHide = RDL.i18n.hidePosts();
		snipType = RDLConstants.SnipType.POST;
		radioBtnParent.clear();
		radioBtnParentProp.clear();
		radioBtnParentProp.removeStyleName("col-lg-6");
		radioBtnParent.removeStyleName("col-lg-6");
	}

	@Override
	protected void setReplyType(){
		replyBean.as().setSnipType(RDLConstants.SnipType.POST);
	}
}
