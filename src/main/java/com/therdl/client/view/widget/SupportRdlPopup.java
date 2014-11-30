package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;

/**
 * Popup shown to the user to become an RDL supporter
 */
public class SupportRdlPopup extends Composite {

	interface SupportRdlPopupUiBinder extends UiBinder<Widget, SupportRdlPopup> {
	}

	private static SupportRdlPopupUiBinder ourUiBinder = GWT.create(SupportRdlPopupUiBinder.class);

	@UiField
	Modal rdlSupporterModal;
	@UiField
	ModalBody modalBody;

	private boolean populated = false;

	public SupportRdlPopup() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public void show() {
		rdlSupporterModal.show();
	}

	public void hide() {
		rdlSupporterModal.hide();
	}

	public void populateBody(AutoBean<SnipBean> contentBean) {
		if (!populated) {
			modalBody.clear();
			modalBody.getElement().setInnerHTML(contentBean.as().getContent());
		}
		populated = true;
	}

	public boolean isPopulated() {
		return populated;
	}

	public void setTitle(String title) {
		rdlSupporterModal.setTitle(title);
	}

}
