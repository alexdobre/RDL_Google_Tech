package com.therdl.client.view.widget;

import java.util.Date;

import org.gwtbootstrap3.client.ui.Badge;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.TribunalDetail;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.Emotion;
import com.therdl.shared.RDLConstants;
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

	@UiField
	HTMLPanel richTextAreaRef;
	@UiField
	Badge creationDate;
	@UiField
	FlowPanel emoListPanel;

	public TribunalCommentRow(AutoBean<SnipBean> commentBean, AutoBean<CurrentUserBean> currentUserBean, TribunalDetail view,
			UIObject parent) {
		this.parent = parent;
		initWidget(ourUiBinder.createAndBindUi(this));
		populate(commentBean, currentUserBean, view);
	}

	public void populate(AutoBean<SnipBean> commentBean, AutoBean<CurrentUserBean> currentUserBean, TribunalDetail view) {
		this.commentBean = commentBean;
		this.view = view;

		richTextAreaRef.getElement().setInnerHTML(commentBean.as().getContent());

		Date date = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN_EXTENDED).parse(commentBean.as().getCreationDate());
		String dateString = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN_EXTENDED).format(date);
		creationDate.setText(dateString);

		emoListPanel.clear();
		if (commentBean.as().getEmotions() != null) {
			for (String emoStr : commentBean.as().getEmotions()) {
				emoListPanel.add(ViewUtils.buildEmoSpan(Emotion.valueOf(emoStr)));
			}
		}

	}

	public UIObject getParentObject() {
		return parent;
	}
}
