package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.TribunalDetail;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Created by dobrea on 29/09/14.
 */
public class TribunalCommentRow extends Composite {
	interface TribunalCommentRowUiBinder extends UiBinder<Widget, TribunalCommentRow> {
	}

	private static TribunalCommentRowUiBinder ourUiBinder = GWT.create(TribunalCommentRowUiBinder.class);

	private AutoBean<SnipBean> commentBean;
	private UIObject parent;
	private TribunalDetail view;

	public TribunalCommentRow(AutoBean<SnipBean> commentBean, AutoBean<CurrentUserBean> currentUserBean, TribunalDetail view,
			UIObject parent) {
		this.parent = parent;
		initWidget(ourUiBinder.createAndBindUi(this));
		populate(commentBean, currentUserBean, view);
	}

	public void populate(AutoBean<SnipBean> commentBean, AutoBean<CurrentUserBean> currentUserBean, TribunalDetail view) {
		this.commentBean = commentBean;
		this.view = view;

		//TODO implement TribunalCommentRow.populate
		// sets values from referenceBean for UI elements from ui binder
		//		richTextAreaRef.getElement().setInnerHTML(referenceBean.as().getContent());
		//		userName.setText(referenceBean.as().getAuthor());
		//		rep.setText(referenceBean.as().getRep().toString());
		//		Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSSS").parse(referenceBean.as().getCreationDate());
		//		String dateString = DateTimeFormat.getFormat("MMM d, y  HH:mm").format(date);
		//		creationDate.setText(dateString);
	}

	public UIObject getParentObject() {
		return parent;
	}
}
