package com.therdl.client.app;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.presenter.runt.impl.ReplyRuntImpl;
import com.therdl.client.view.ValidatedView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Provides ready made implementations of Runt interfaces
 */
public class RuntFactory {

	public static ReplyRunt createReplyRunt(AutoBean<CurrentUserBean> currentUserBean, ValidatedView validatedView) {
		return new ReplyRuntImpl(currentUserBean, validatedView);
	}
}
