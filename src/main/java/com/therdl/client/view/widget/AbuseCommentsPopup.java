package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.List;

/**
 * Popup contains previous abuse comments
 */
public class AbuseCommentsPopup extends Composite {

	interface AbuseCommentsPopupUiBinder extends UiBinder<Widget, AbuseCommentsPopup> {
	}

	private static AbuseCommentsPopupUiBinder ourUiBinder = GWT.create(AbuseCommentsPopupUiBinder.class);

	@UiField
	Modal abuseCommentsModal;
	@UiField
	ModalBody modalBody;

	private boolean populated = false;

	public AbuseCommentsPopup() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public void show() {
		abuseCommentsModal.show();
	}

	public void hide() {
		abuseCommentsModal.hide();
	}

	public void populateBody(List<String> comments) {
		modalBody.clear();
		for (String comment : comments) {
			modalBody.add(new Paragraph(comment));
			modalBody.add(new HTML("<hr  style=\"width:100%;\" />"));
		}
	}

}
