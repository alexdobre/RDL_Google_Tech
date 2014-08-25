package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Modal;

import java.util.logging.Logger;

/**
 * Popup shown to the user to become an RDL supporter
 */
public class SupportRdlPopup extends Composite {
	private static Logger log = Logger.getLogger(SupportRdlPopup.class.getName());

	interface SupportRdlPopupUiBinder extends UiBinder<Widget, SupportRdlPopup> {
	}

	private static SupportRdlPopupUiBinder ourUiBinder = GWT.create(SupportRdlPopupUiBinder.class);

	@UiField
	Modal rdlSupporterModal;

	public SupportRdlPopup() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public void show(){
		rdlSupporterModal.show();
	}

	public void hide(){
		rdlSupporterModal.hide();
	}

}
