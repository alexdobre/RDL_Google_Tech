package com.therdl.client.view.widget.runtized;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.EmotionView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.EmotionPicker;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Use edits their reply
 */
public class ReplyEditWidget extends Composite implements EmotionView {

	interface ReplyEditWidgetUiBinder extends UiBinder<Widget, ReplyEditWidget> {
	}

	private static ReplyEditWidgetUiBinder ourUiBinder = GWT.create(ReplyEditWidgetUiBinder.class);

	@UiField
	FlowPanel emoListPanel;
	@UiField
	Summernote richTextEditor;
	@UiField
	Button updateRef, cancelUpdateRef;

	private AutoBean<SnipBean> referenceBean;
	private AutoBean<CurrentUserBean> currentUserBean;
	private ReplyRunt replyRunt;
	private EmotionPicker emotionPicker;

	public ReplyEditWidget(ReplyRunt replyRunt) {
		initWidget(ourUiBinder.createAndBindUi(this));
		this.replyRunt = replyRunt;
	}

	public void populate(AutoBean<SnipBean> referenceBean, AutoBean<CurrentUserBean> currentUserBean) {
		this.referenceBean = referenceBean;
		this.currentUserBean = currentUserBean;
		ViewUtils.displayEmotions(emoListPanel, referenceBean);
		richTextEditor.setCode(referenceBean.as().getContent());
		if (emotionPicker == null) {
			emotionPicker = new EmotionPicker(this, referenceBean);
		} else {
			emotionPicker.setCurrentSnipBean(referenceBean);
		}
	}

	public void setReplyRunt(ReplyRunt replyRunt) {
		this.replyRunt = replyRunt;
	}

	@Override
	public void displayEmotions() {
		ViewUtils.displayEmotions(emoListPanel, referenceBean);
	}

	@UiHandler("updateRef")
	void onUpdateRef(ClickEvent event) {
		referenceBean.as().setContent(richTextEditor.getCode());
		replyRunt.saveEditedReference();
	}

	@UiHandler("cancelUpdateRef")
	void onCancelUpdateRef(ClickEvent event) {
		replyRunt.cancelEditReference();
	}

	@UiHandler("btnEmotionPicker")
	void onEmotionPicker(ClickEvent event) {
		emotionPicker.show();
	}
}
