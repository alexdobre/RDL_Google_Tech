package com.therdl.client.view.widget.runtized;

import java.util.Date;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.impl.AbstractValidatedView;
import com.therdl.shared.Emotion;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.ReportAbuseEvent;
import com.therdl.shared.events.ShowAbuseCommentsEvent;

/**
 * gwt widget class for reference row in snip view page
 */
public class ReferenceListRow extends AbstractValidatedView implements ValidatedView {

	interface ReferenceListRowUiBinder extends UiBinder<HTMLPanel, ReferenceListRow> {
	}

	private static ReferenceListRowUiBinder ourUiBinder = GWT.create(ReferenceListRowUiBinder.class);

	private AutoBean<SnipBean> referenceBean;
	private ReplyRunt replyRunt;

	@UiField
	HTMLPanel richTextAreaRef;
	@UiField
	Badge rep, userName, creationDate;
	@UiField
	Label refFlag;
	@UiField
	Image avatarImg;
	@UiField
	Button reportAbuse;
	@UiField
	FlowPanel emoListPanel;
	@UiField
	Paragraph abuseWarning;
	@UiField
	Anchor abuseWarningAnchor, userNameLink;
	@UiField
	SimplePanel snipActionPanel;
	@UiField
	Column refContent, editRefContent;

	private UIObject parent;

	public ReferenceListRow(AutoBean<SnipBean> referenceBean, AutoBean<CurrentUserBean> currentUserBean,
	                        UIObject parent, ReplyRunt replyRunt) {
		this.parent = parent;
		initWidget(ourUiBinder.createAndBindUi(this));
		snipActionPanel.getElement().getStyle().setProperty("zIndex","1000");
		populate(referenceBean, currentUserBean, replyRunt);
	}

	public void populate(AutoBean<SnipBean> referenceBean, AutoBean<CurrentUserBean> currentUserBean,
			ReplyRunt replyRunt) {
		this.referenceBean = referenceBean;
		this.replyRunt = replyRunt;

		// sets values from referenceBean for UI elements from ui binder
		richTextAreaRef.getElement().setInnerHTML(referenceBean.as().getContent());
		rep.setText(referenceBean.as().getRep().toString());
		Date date = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN_HYPER_EXTENDED).parse(referenceBean.as().getCreationDate());
		String dateString = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN_EXTENDED).format(date);
		creationDate.setText(dateString);

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS) || Global.moduleName.equals(RDLConstants.Modules.SERVICES)) {
			if (referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.POSITIVE))
				refFlag.setText(RDL.i18n.positive());
			else if (referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEUTRAL))
				refFlag.setText(RDL.i18n.neutral());
			else if (referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEGATIVE))
				refFlag.setText(RDL.i18n.negative());

			refFlag.getElement().getStyle().setProperty("backgroundColor", RDLConstants.ReferenceType.colorCodes.get(referenceBean.as().getReferenceType()));
		} else {

			if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
				ViewUtils.hide(refFlag);
			} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
				if (referenceBean.as().getSnipType().equals(RDLConstants.SnipType.PLEDGE)) {
					refFlag.setText(RDL.i18n.pledge());
					refFlag.getElement().getStyle().setProperty("backgroundColor", RDLConstants.ReferenceType.colorCodes.get(RDLConstants.ReferenceType.POSITIVE));
				}
				if (referenceBean.as().getSnipType().equals(RDLConstants.SnipType.COUNTER)) {
					refFlag.setText(RDL.i18n.counter());
					refFlag.getElement().getStyle().setProperty("backgroundColor", RDLConstants.ReferenceType.colorCodes.get(RDLConstants.ReferenceType.NEGATIVE));
				}
			}
		}
		if (referenceBean.as().getAuthorSupporter()) {
			ViewUtils.showHide(true, avatarImg);
			avatarImg.setUrl(ViewUtils.getAvatarImageUrl(referenceBean.as().getAuthor()));
		} else {
			ViewUtils.showHide(false, avatarImg);
		}

		if (referenceBean.as().getAuthorSupporter()) {
			userName.setText("");
			userNameLink.setHref("#" + RDLConstants.Tokens.PUBLIC_PROFILE + ":" + referenceBean.as().getAuthor());
			userNameLink.setText(referenceBean.as().getAuthor());
		} else {
			userName.setText(referenceBean.as().getAuthor());
			userNameLink.setText("");
			userNameLink.setHref("");
		}
		emoListPanel.clear();
		if (referenceBean.as().getEmotions() != null) {
			for (String emoStr : referenceBean.as().getEmotions()) {
				emoListPanel.add(ViewUtils.buildEmoSpan(Emotion.valueOf(emoStr)));
			}
		}
		ViewUtils.showHide(ViewUtils.showReportAbuseLogic(currentUserBean, referenceBean), reportAbuse);
		ViewUtils.showHide(referenceBean.as().getAbuseCount()!=null && referenceBean.as().getAbuseCount()>0,
				abuseWarning);
		ViewUtils.hide(editRefContent);
		editRefContent.clear();
		ViewUtils.show(refContent);
		hideMessages();
	}

	public UIObject getParentObject() {
		return parent;
	}

	@UiHandler("reportAbuse")
	public void onReportAbuseClicked(ClickEvent event) {
		GuiEventBus.EVENT_BUS.fireEvent(new ReportAbuseEvent(referenceBean.as().getId()));
	}

	@UiHandler("abuseWarningAnchor")
	public void abuseWarningClick(ClickEvent event) {
		GuiEventBus.EVENT_BUS.fireEvent(new ShowAbuseCommentsEvent(referenceBean.as()));
	}

	public void setReplyRunt( ReplyRunt replyRunt) {
		this.replyRunt = replyRunt;
	}

	public SimplePanel getSnipActionPanel () {
		return snipActionPanel;
	}

	public AutoBean<SnipBean> getReferenceBean() {
		return referenceBean;
	}

	public Column getRefContent() {
		return refContent;
	}

	public Column getEditRefContent() {
		return editRefContent;
	}


}