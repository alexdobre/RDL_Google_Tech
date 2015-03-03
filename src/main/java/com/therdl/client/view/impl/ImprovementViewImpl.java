package com.therdl.client.view.impl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.ImprovementView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The improvement view
 */
public class ImprovementViewImpl extends SnipViewImpl implements ImprovementView {
	public ImprovementViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(currentUserBean, appMenu);

		btnTextShow = RDL.i18n.showPosts();
		btnTextHide = RDL.i18n.hidePosts();
		snipType = RDLConstants.SnipType.PLEDGE + "," + RDLConstants.SnipType.COUNTER;
		radioBtnParent.clear();
		radioBtnParent.removeStyleName("col-lg-6");
	}

	@Override
	protected void setReplyType(){
		if (prb2.getValue()) {
			replyBean.as().setSnipType(RDLConstants.SnipType.COUNTER);
		}else {
			replyBean.as().setSnipType(RDLConstants.SnipType.PLEDGE);
		}
	}

	
}
