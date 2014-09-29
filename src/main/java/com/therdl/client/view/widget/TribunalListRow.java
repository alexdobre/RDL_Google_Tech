package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.ProgressType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.logging.Logger;

/**
 * The tribunal list row
 */
public class TribunalListRow extends AbstractListRow {

	private static Logger log = Logger.getLogger(TribunalListRow.class.getName());

	interface TribunalListRowUiBinder extends UiBinder<Widget, TribunalListRow> {
	}

	private static TribunalListRowUiBinder ourUiBinder = GWT.create(TribunalListRowUiBinder.class);

	@UiField
	ProgressBar yesAbuseProgress, noAbuseProgress;
	@UiField
	Paragraph fraction;
	@UiField
	Progress tribunalRowProgress;
	@UiField
	Badge votingText;

	public TribunalListRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType,
	                       UIObject parent) {
		super(snipBean, currentUserBean, snipType, parent);
		initWidget(ourUiBinder.createAndBindUi(this));
		populate(snipBean, currentUserBean, snipType);
	}

	public TribunalListRow() {
		//used for a seed instance
	}

	@Override
	public void populate(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType) {
		log.info("TribunalListRow populate with title: " + snipBean.as().getTitle());
		this.snipBean = snipBean;
		this.currentUserBean = currentUserBean;
		this.snipType = snipType;

		doBackgroundColor();
		doTexts();
		doProgress();
		setActive();
		doImages(snipBean.as().getPreviousSnipType());
	}

	@Override
	public AbstractListRow makeRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType, UIObject parent) {
		return new TribunalListRow(snipBean, currentUserBean, snipType, parent);
	}

	private void doProgress() {
		double yes, no;
		yes = snipBean.as().getAbuseCount() != null ? snipBean.as().getAbuseCount() : 0;
		no = snipBean.as().getNoAbuseCount() != null ? snipBean.as().getNoAbuseCount() : 0;
		yesAbuseProgress.setPercent(yes / (yes + no) * 100);
		noAbuseProgress.setPercent(no / (yes + no) * 100);
	}

	private void doTexts() {
		snipTitle.setText(snipBean.as().getTitle());
		snipTitle.setHref("#" + RDLConstants.Tokens.TRIBUNAL_DETAIL + ":" + snipBean.as().getId());

		if (snipBean.as().getVotingExpiresDate() != null) {
			displayDate.setText(snipBean.as().getVotingExpiresDate());
		}

		String yesAbuse, noAbuse;
		if (snipBean.as().getAbuseCount() != null) {
			yesAbuse = snipBean.as().getAbuseCount().toString();
		} else {
			yesAbuse = "0";
		}
		if (snipBean.as().getNoAbuseCount() != null) {
			noAbuse = snipBean.as().getNoAbuseCount().toString();
		} else {
			noAbuse = "0";
		}
		fraction.setText(yesAbuse + "/" + noAbuse);
	}

	private void setActive(){
		if (ViewUtils.isExpired(snipBean.as().getVotingExpiresDate())){
			tribunalRowProgress.setActive(false);
			tribunalRowProgress.setType(ProgressType.DEFAULT);
			votingText.setText(RDL.getI18n().votingHasEndedOn());
		} else {
			tribunalRowProgress.setActive(true);
			tribunalRowProgress.setType(ProgressType.STRIPED);
			votingText.setText(RDL.getI18n().votingEndsOn());
		}
	}
}
