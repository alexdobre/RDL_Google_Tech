package com.therdl.client.presenter;

import com.therdl.client.view.widget.AbuseCommentsPopup;
import com.therdl.client.view.widget.SupportRdlPopup;
import com.therdl.shared.beans.SnipBean;

/**
 * Holds methods that could be used in common by several presenters
 */
public interface CommonPresenter extends Presenter{

	void grabRdlSupporterTitleDesc(final SupportRdlPopup supportRdlPopup);

	void reportAbuse(String contentId, String reason);

	public void showAbuseComments(SnipBean abusiveContent, AbuseCommentsPopup popup);
}
