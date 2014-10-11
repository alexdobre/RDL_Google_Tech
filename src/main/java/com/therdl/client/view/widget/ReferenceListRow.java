package com.therdl.client.view.widget;

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
import com.google.gwt.user.client.ui.UIObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.Emotion;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.ReportAbuseEvent;
import com.therdl.shared.events.ShowAbuseCommentsEvent;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.Date;

/**
 * gwt widget class for reference row in snip view page
 */
public class ReferenceListRow extends Composite {
	interface ReferenceListRowUiBinder extends UiBinder<HTMLPanel, ReferenceListRow> {
	}

	private static ReferenceListRowUiBinder ourUiBinder = GWT.create(ReferenceListRowUiBinder.class);

	private AutoBean<SnipBean> referenceBean;

	@UiField
	HTMLPanel richTextAreaRef;
	@UiField
	Badge rep, userName, creationDate;
	@UiField
	Label refFlag;
	@UiField
	Image avatarImg;
	@UiField
	Button refRepBtn, reportAbuse;
	@UiField
	FlowPanel emoListPanel;
	@UiField
	Paragraph abuseWarning;
	@UiField
	Anchor abuseWarningAnchor, userNameLink;

	SnipView view;

	private UIObject parent;

	public ReferenceListRow(AutoBean<SnipBean> referenceBean, AutoBean<CurrentUserBean> currentUserBean, SnipView view,
	                        UIObject parent) {
		this.parent = parent;
		initWidget(ourUiBinder.createAndBindUi(this));
		populate(referenceBean, currentUserBean, view);
	}

	public void populate(AutoBean<SnipBean> referenceBean, AutoBean<CurrentUserBean> currentUserBean, SnipView view) {
		this.referenceBean = referenceBean;
		this.view = view;

		// sets values from referenceBean for UI elements from ui binder
		richTextAreaRef.getElement().setInnerHTML(referenceBean.as().getContent());
		rep.setText(referenceBean.as().getRep().toString());
		Date date = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN_HYPER_EXTENDED).parse(referenceBean.as().getCreationDate());
		String dateString = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN_EXTENDED).format(date);
		creationDate.setText(dateString);

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			if (referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.POSITIVE))
				refFlag.setText(RDL.i18n.positive());
			else if (referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEUTRAL))
				refFlag.setText(RDL.i18n.neutral());
			else if (referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEGATIVE))
				refFlag.setText(RDL.i18n.negative());

			refFlag.getElement().getStyle().setProperty("backgroundColor", RDLConstants.ReferenceType.colorCodes.get(referenceBean.as().getReferenceType()));

			ViewUtils.hide(refRepBtn);
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

			if (!currentUserBean.as().isAuth() || referenceBean.as().getAuthor().equals(currentUserBean.as().getName()) || (referenceBean.as().getIsRepGivenByUser() != null && referenceBean.as().getIsRepGivenByUser() == 1)) {
				ViewUtils.hide(refRepBtn);
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
	}

	public UIObject getParentObject() {
		return parent;
	}

	@UiHandler("refRepBtn")
	public void onRepBtnClicked(ClickEvent event) {
		view.getPresenter().giveSnipReputation(referenceBean.as().getId(), new RequestObserver() {
			@Override
			public void onSuccess(String response) {
				ViewUtils.hide(refRepBtn);
				rep.setText(" " + (referenceBean.as().getRep() + 1));
			}
		});
	}

	@UiHandler("reportAbuse")
	public void onReportAbuseClicked(ClickEvent event) {
		GuiEventBus.EVENT_BUS.fireEvent(new ReportAbuseEvent(referenceBean.as().getId()));
	}

	@UiHandler("abuseWarningAnchor")
	public void abuseWarningClick(ClickEvent event) {
		GuiEventBus.EVENT_BUS.fireEvent(new ShowAbuseCommentsEvent(referenceBean.as()));
	}
}