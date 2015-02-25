package com.therdl.client.view.impl;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ServiceView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SocialPanel;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The service view
 */
public class ServiceViewImpl extends SnipViewImpl implements ServiceView {

		
	public ServiceViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(currentUserBean, appMenu);
		radioBtnParentProp.clear();
		radioBtnParentProp.removeStyleName("col-lg-6");
	}

	@Override
	protected void setReplyType() {
		String referenceType = RDLConstants.ReferenceType.POSITIVE;
		if (rb2.getValue()) {
			referenceType = RDLConstants.ReferenceType.NEUTRAL;
		} else if (rb3.getValue()) {
			referenceType = RDLConstants.ReferenceType.NEGATIVE;
		}
		replyBean.as().setReferenceType(referenceType);
		replyBean.as().setSnipType(RDLConstants.SnipType.REFERENCE);
	}
}
