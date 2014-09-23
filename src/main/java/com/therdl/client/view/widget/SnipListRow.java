package com.therdl.client.view.widget;

import java.util.Date;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.impl.AbstractListRow;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * gwt widget for snip list row
 * constructor gets snipBean and logged userBean
 */
public class SnipListRow extends AbstractListRow {
	private static Logger log = Logger.getLogger(SnipListRow.class.getName());

	interface SnipListRowUiBinder extends UiBinder<Widget, SnipListRow> {
	}

	private static SnipListRowUiBinder ourUiBinder = GWT.create(SnipListRowUiBinder.class);

	@UiField
	Label proposalType, proposalState;
	@UiField
	Badge postsCount, rep, posRef, neutRef, negRef, pledgesCount, countersCount;
	@UiField
	Image snipImg;
	@UiField
	Row likesRepliesPanel, referencesPanel, pledgeCounterPanel;

	public SnipListRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType,
			UIObject parent) {
		super(snipBean, currentUserBean, snipType, parent);
		initWidget(ourUiBinder.createAndBindUi(this));
		populate(snipBean, currentUserBean, snipType);
	}

	public SnipListRow() {
		//used for a seed instance
	}

	@Override
	public void populate(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType) {
		log.info("SnipListRow populate with title: " + snipBean.as().getTitle());
		this.snipBean = snipBean;
		this.currentUserBean = currentUserBean;
		this.snipType = snipType;

		doBackgroundColor();
		doTexts();
		doImages();
		showHide();
	}

	private void showHide() {
		ViewUtils.hide(likesRepliesPanel);
		ViewUtils.hide(referencesPanel);
		ViewUtils.hide(pledgeCounterPanel);

		if (snipType == null) {
			snipType = SnipType.IMPROVEMENT;
		}

		if (snipType.isIdea()) {
			ViewUtils.show(referencesPanel);
		} else if (snipType.isStory()) {
			ViewUtils.show(likesRepliesPanel);
		} else if (snipType.isImprovement()) {
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
		if (snipBean.as().getAuthorSupporter()) {
			ViewUtils.showHide(true, avatarImg);
			avatarImg.setUrl(ViewUtils.getAvatarImageUrl(snipBean.as().getAuthor()));
		} else {
			ViewUtils.showHide(false, avatarImg);
		}
	}

	private void doTexts() {
		userName.setText(snipBean.as().getAuthor());
		rep.setText(snipBean.as().getRep() + "");
		snipTitle.setText(snipBean.as().getTitle());

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
			snipTitle.setHref("#" + RDLConstants.Tokens.SNIP_VIEW + ":" + snipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
			snipTitle.setHref("#" + RDLConstants.Tokens.THREAD_VIEW + ":" + snipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
			snipTitle.setHref("#" + RDLConstants.Tokens.PROPOSAL_VIEW + ":" + snipBean.as().getId());

		posRef.setText(snipBean.as().getPosRef().toString());
		neutRef.setText(snipBean.as().getNeutralRef().toString());
		negRef.setText(snipBean.as().getNegativeRef().toString());
		postsCount.setText(snipBean.as().getPosts().toString());
		pledgesCount.setText(snipBean.as().getPledges().toString());
		countersCount.setText(snipBean.as().getCounters().toString());

		if (snipBean.as().getCreationDate() != null) {
			Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSSS").parse(snipBean.as().getCreationDate());
			String dateString = DateTimeFormat.getFormat("MMM d, y  HH:mm").format(date);
			creationDate.setText(dateString);
		}

		proposalType.setText(RDL.i18n.type() + ": " + snipBean.as().getProposalType());
		proposalState.setText(RDL.i18n.state() + ": " + snipBean.as().getProposalState());
	}



	public void incrementRepCounter() {
		rep.setText(snipBean.as().getRep() + 1 + "");
	}

	public void incrementRefCounterByRefType(String refType, String snipType) {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			if (refType.equals(RDLConstants.ReferenceType.POSITIVE))
				posRef.setText(snipBean.as().getPosRef() + 1 + "");
			else if (refType.equals(RDLConstants.ReferenceType.NEUTRAL))
				neutRef.setText(snipBean.as().getNeutralRef() + 1 + "");
			else if (refType.equals(RDLConstants.ReferenceType.NEGATIVE))
				negRef.setText(snipBean.as().getNegativeRef() + 1 + "");
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			postsCount.setText(snipBean.as().getPosts() + 1 + "");
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			if (snipType.equals(RDLConstants.SnipType.PLEDGE))
				pledgesCount.setText(snipBean.as().getPledges() + 1 + "");
			else if (snipType.equals(RDLConstants.SnipType.COUNTER))
				countersCount.setText(snipBean.as().getCounters() + 1 + "");
		}
	}

	public AbstractListRow makeRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean,
			SnipType snipType, UIObject parent){
		return new SnipListRow(snipBean, currentUserBean, snipType, parent);
	}
}