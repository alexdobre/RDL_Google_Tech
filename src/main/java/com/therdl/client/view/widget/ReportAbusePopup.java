package com.therdl.client.view.widget;

import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.presenter.CommonPresenter;

/**
 * Popup shown to user when abuse is reported
 */
public class ReportAbusePopup extends Composite {

	private static Logger log = Logger.getLogger(ReportAbusePopup.class.getName());

	interface ReportAbusePopupUiBinder extends UiBinder<Widget, ReportAbusePopup> {
	}

	private static ReportAbusePopupUiBinder ourUiBinder = GWT.create(ReportAbusePopupUiBinder.class);

	@UiField
	Modal reportAbuseModal;
	@UiField
	TextArea abuseReason;
	@UiField
	Button cancel, reportAbuse;

	private CommonPresenter presenter;
	private String contentId;


	public ReportAbusePopup(CommonPresenter presenter) {
		initWidget(ourUiBinder.createAndBindUi(this));
		this.presenter = presenter;
	}

	public void show(String contentId) {
		this.contentId = contentId;
		abuseReason.clear();
		reportAbuseModal.show();
	}

	public void hide() {
		reportAbuseModal.hide();
	}

	@UiHandler("cancel")
	public void onCancelClicked(ClickEvent event) {
		hide();
	}

	@UiHandler("reportAbuse")
	public void onReportAbuseClicked(ClickEvent event) {
		presenter.reportAbuse(contentId, abuseReason.getText());
	}
}
