package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.common.SnipType;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.Tooltip;

import java.util.Date;
import java.util.logging.Logger;

/**
 * gwt widget for snip list row
 * constructor gets snipBean and logged userBean
 */
public class SnipListRow extends Composite {
	private static Logger log = Logger.getLogger("");

	private static final int TITLE_MAX_LENGTH = 60;

	interface SnipListRowUiBinder extends UiBinder<HTMLPanel, SnipListRow> {
	}

	private static SnipListRowUiBinder ourUiBinder = GWT.create(SnipListRowUiBinder.class);

	private AutoBean<SnipBean> snipBean;
	private AutoBean<CurrentUserBean> currentUserBean;
	private SnipType snipType;

	@UiField
	FlowPanel colorStripe, snipImgParent;
	@UiField
	Label pledgesCount, countersCount, proposalType, proposalState;
	@UiField
	Badge creationDate, viewCount, postsCount, userName, rep, posRef, neutRef, negRef;
	@UiField
	Image snipImg;
	@UiField
	Anchor snipTitle;
	@UiField
	Tooltip snipTitleTooltip, snipImageTooltip;
	@UiField
	Row likesRepliesPanel, referencesPanel, pledgeCounterPanel;

	public SnipListRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType) {
		initWidget(ourUiBinder.createAndBindUi(this));
		this.snipBean = snipBean;
		this.currentUserBean = currentUserBean;
		this.snipType = snipType;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		doBackgroundColor();
		doTexts();
		doImages();
		showHide();
	}

	private void showHide() {
		ViewUtils.hide(likesRepliesPanel);
		ViewUtils.hide(referencesPanel);
		ViewUtils.hide(pledgeCounterPanel);

		if (snipType.isIdea()){
			ViewUtils.show(referencesPanel);
		}else if (snipType.isStory()){
			ViewUtils.show(likesRepliesPanel);
		}else if (snipType.isImprovement()){
			ViewUtils.show(pledgeCounterPanel);
		}
	}

	private void doImages() {
		if (snipBean.as().getSnipType().equals(RDLConstants.SnipType.SNIP))
			snipImg.setUrl(Resources.INSTANCE.SnipImage().getSafeUri().asString());
		if (snipBean.as().getSnipType().equals(RDLConstants.SnipType.FAST_CAP))
			snipImg.setUrl(Resources.INSTANCE.FastCapImage().getSafeUri().asString());
		if (snipBean.as().getSnipType().equals(RDLConstants.SnipType.HABIT))
			snipImg.setUrl(Resources.INSTANCE.HabitImage().getSafeUri().asString());
		if (snipBean.as().getSnipType().equals(RDLConstants.SnipType.MATERIAL))
			snipImg.setUrl(Resources.INSTANCE.MaterialImage().getSafeUri().asString());
		if (snipBean.as().getSnipType().equals(RDLConstants.SnipType.THREAD))
			snipImg.setUrl(Resources.INSTANCE.ThreadImageGif().getSafeUri().asString());
		if (snipBean.as().getSnipType().equals(RDLConstants.SnipType.PROPOSAL))
			snipImg.setUrl(Resources.INSTANCE.ProposalImageGif().getSafeUri().asString());
	}

	private void doTexts() {
		userName.setText(snipBean.as().getAuthor());

		// set tooltip on the snip img and top color stripe

		String toolTip = snipBean.as().getSnipType() + " / " + snipBean.as().getCoreCat();

		// sets snip data
		snipImageTooltip.setTitle(toolTip);
		colorStripe.setTitle(toolTip);

		rep.setText(snipBean.as().getRep() + "");
		displaySnipTitle();

		posRef.setText(snipBean.as().getPosRef().toString());
		neutRef.setText(snipBean.as().getNeutralRef().toString());
		negRef.setText(snipBean.as().getNegativeRef().toString());
		viewCount.setText(snipBean.as().getViews() + " ");
		postsCount.setText(snipBean.as().getPosts() + " ");
		pledgesCount.setText(snipBean.as().getPledges() + " " + RDL.i18n.pledges());
		countersCount.setText(snipBean.as().getCounters() + " " + RDL.i18n.counters());

		Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSSS").parse(snipBean.as().getCreationDate());
		String dateString = DateTimeFormat.getFormat("MMM d, y  HH:mm").format(date);
		creationDate.setText(dateString);

		proposalType.setText(RDL.i18n.type() + ": " + snipBean.as().getProposalType());
		proposalState.setText(RDL.i18n.state() + ": " + snipBean.as().getProposalState());
	}

	private void doBackgroundColor() {
		// sets background color for snip img and top color strip
		if (!Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			for (CoreCategory item : CoreCategory.values()) {
				if (item.getShortName().equals(snipBean.as().getCoreCat())) {
					colorStripe.getElement().getStyle().setProperty("backgroundColor", item.getColCode());
					snipImgParent.getElement().getStyle().setProperty("backgroundColor", item.getColCode());
				}
			}
		} else {
			colorStripe.getElement().getStyle().setProperty("backgroundColor", "#658cd9");
			snipImgParent.getElement().getStyle().setProperty("backgroundColor", "#658cd9");
		}
	}

	private void displaySnipTitle() {
		String snipTitleString = snipBean.as().getTitle();
		if (snipTitleString.length() <= TITLE_MAX_LENGTH) {
			snipTitle.setText(snipTitleString);
		} else {
			snipTitle.setText(snipTitleString.substring(0, 56) + "...");
			snipTitleTooltip.setTitle(snipTitleString);
		}
	}

	@UiHandler("snipTitle")
	public void snipTitleCliked(ClickEvent event) {
		triggerViewSnip();
	}

	private void triggerViewSnip() {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
			History.newItem(RDLConstants.Tokens.SNIP_VIEW + ":" + snipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
			History.newItem(RDLConstants.Tokens.THREAD_VIEW + ":" + snipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
			History.newItem(RDLConstants.Tokens.PROPOSAL_VIEW + ":" + snipBean.as().getId());
	}

	public void incrementRepCounter() {
		rep.setText(snipBean.as().getRep() + 1 + "");
	}

	public void incrementRefCounterByRefType(String refType, String snipType) {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			if (refType.equals(RDLConstants.ReferenceType.POSITIVE))
				posRef.setText(snipBean.as().getPosRef() + 1 + " " + RDL.i18n.positiveRef());
			else if (refType.equals(RDLConstants.ReferenceType.NEUTRAL))
				neutRef.setText(snipBean.as().getNeutralRef() + 1 + " " + RDL.i18n.neutralRef());
			else if (refType.equals(RDLConstants.ReferenceType.NEGATIVE))
				negRef.setText(snipBean.as().getNegativeRef() + 1 + " " + RDL.i18n.negativeRef());
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			postsCount.setText(snipBean.as().getPosts() + 1 + " " + RDL.i18n.posts());
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			if (snipType.equals(RDLConstants.SnipType.PLEDGE))
				pledgesCount.setText(snipBean.as().getPledges() + 1 + " " + RDL.i18n.pledges());
			else if (snipType.equals(RDLConstants.SnipType.COUNTER))
				countersCount.setText(snipBean.as().getCounters() + 1 + " " + RDL.i18n.counters());
		}
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}
}