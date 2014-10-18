package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;

import java.util.logging.Logger;

/**
 * A simple collapsible panel to hold an FAQ question
 */
public class FaqPanel extends Composite {

	private static Logger log = Logger.getLogger(FaqPanel.class.getName());

	interface FaqPanelUiBinder extends UiBinder<Widget, FaqPanel> {
	}

	private static FaqPanelUiBinder ourUiBinder = GWT.create(FaqPanelUiBinder.class);

	@UiField
	Anchor question;
	@UiField
	PanelCollapse answer;
	@UiField
	PanelBody answerBody;

	public FaqPanel() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	/**
	 * Populates the FAQ panel with the question and answer
	 *
	 * @param faqBean
	 */
	public void populate(AutoBean<SnipBean> faqBean) {
		question.setText(faqBean.as().getTitle());
		question.setDataTarget("#"+faqBean.as().getId());
		answer.setId(faqBean.as().getId());
		answerBody.getElement().setInnerHTML(faqBean.as().getContent());
	}
}
