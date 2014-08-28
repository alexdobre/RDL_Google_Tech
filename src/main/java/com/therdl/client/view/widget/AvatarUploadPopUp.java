package com.therdl.client.view.widget;

import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.impl.AbstractValidatedView;
import com.therdl.client.view.impl.ProfileViewImpl;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * this class will handle the file upload as a pop up
 * triggered by clicking on the avatar image place holder
 * this class extends PopupPanel to implement a custom GWT widget
 *
 * @ HTMLPanel profileUpLoadFormPanel, the container for the upload form upload
 * @ FormPanel uploadForm, GWT form upload panel
 * see http://www.gwtapps.com/doc/html/com.google.gwt.user.client.ui.FormPanel.html
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ ProfileViewImpl mainView, the parent container,
 * as this class is a pop up it needs a reference to its parent container to call back to
 */
public class AvatarUploadPopUp extends AbstractValidatedView {

	private static Logger sLogger = Logger.getLogger("AvatarUploadPopUp");

	interface AvatarUploadPopUpUiBinder extends UiBinder<Widget, AvatarUploadPopUp> {
	}

	private static AvatarUploadPopUpUiBinder uiBinder = GWT.create(AvatarUploadPopUpUiBinder.class);

	@UiField
	Modal profileUpLoadFormPanel;

	@UiField
	FormPanel uploadForm;

	@UiField
	FileUpload fileUpload;

	@UiField
	Button submitBtn;

	// to handle data view and events for this pop up
	ProfileViewImpl mainView;

	public AvatarUploadPopUp(AutoBean<CurrentUserBean> cUserBean, ProfileViewImpl mainView) {
		this.mainView = mainView;
		uiBinder.createAndBindUi(this);
		setUploadForm();
	}

	public void show() {
		profileUpLoadFormPanel.show();
	}


	/**
	 * note as we are using the gwt form upload widget we break the mvp here in a small way only
	 * FileUpload upload see http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/FileUpload.html
	 * FileUpload wraps the HTML <input type='file'> element.
	 */
	private void setUploadForm() {
		String uploadUrl = GWT.getModuleBaseURL() + "avatarUpload";

		uploadForm.setAction(uploadUrl);
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		fileUpload.setName("fileElement");

		// Add an event handler to the form.
		uploadForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(FormPanel.SubmitEvent event) {

			}
		});
		uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
				sLogger.info("Apparently file uploaded successfully"+event.getResults());
				if (event.getResults().contains("ERROR")){
					setErrorMessage(RDL.getI18n().imageUploadError());
				}else {
					profileUpLoadFormPanel.hide();
					mainView.refreshImage();
				}
			}
		});

		uploadForm.setStyleName("uploadForm");
	}

	@UiHandler("submitBtn")
	void handleSubmit(ClickEvent e) {
		uploadForm.submit();
	}

}
