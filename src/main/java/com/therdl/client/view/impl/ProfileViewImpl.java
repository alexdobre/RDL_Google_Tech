package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.AvatarUploadPopUp;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SummerCustomToolbar;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.BecomeRdlSupporterEvent;
import com.therdl.shared.events.GuiEventBus;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

import java.util.Date;

/**
 * ProfileViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class will be extended to encapsulate all the profile related data for the
 * client for example the User Avatar and Avatar upload
 *
 * @ ProfileView.Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ AutoBean<CurrentUserBean> currentUserBean contains user parameters like auth state
 * @ AvatarUploadPopUp uploadForm the upload form widget
 * @ FocusPanel profileImagePanel   GWT FocusPanel allows events for a image container
 * see http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/FocusPanel.html
 * @ Image pic  the users avatar image
 * @ String tempUrl displays a empty avatar image to indicate where a user Avatar would appear if it was uploded,
 * also to prompt the uses to click to initiate the upload
 */
public class ProfileViewImpl extends AbstractValidatedAppMenuView implements ProfileView {

	private AutoBean<CurrentUserBean> currentUserBean;

	interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl> {
	}

	private static ProfileViewImplUiBinder uiBinder = GWT.create(ProfileViewImplUiBinder.class);

	private ProfileView.Presenter presenter;

	private AvatarUploadPopUp uploadForm;

	@UiField
	FocusPanel profileImagePanel;
	@UiField
	Button supporterBtn, updateProfileDec, changePassBtn, submitChangePassBtn;
	@UiField
	FormControlStatic email, username, rep;
	@UiField
	Image avatarImage;
	@UiField
	Modal changePassModal;
	@UiField
	Paragraph titleExpiresParagraph;
	@UiField
	Input oldPassword, newPassword, confirmNewPassword;
	@UiField(provided = true)
	Summernote profileDesc;
	@UiField
	Anchor viewPublicProfile;

	public ProfileViewImpl(AutoBean<CurrentUserBean> cUserBean, AppMenu appMenu) {
		super(appMenu);
		profileDesc = new Summernote();
		profileDesc.setToolbar(new SummerCustomToolbar());
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = cUserBean;
		populateView(currentUserBean);
	}

	public void populateView(AutoBean<CurrentUserBean> currentUserBean) {
		if (currentUserBean != null && currentUserBean.as() != null) {
			this.currentUserBean = currentUserBean;
			email.setText(currentUserBean.as().getEmail());
			username.setText(currentUserBean.as().getName());
			if (currentUserBean.as().getRep() != null) {
				rep.setText(currentUserBean.as().getRep().toString());
			} else {
				rep.setText("0");
			}
			supporterBtn.setVisible(true);
			ViewUtils.showHide(false, formErrors);
			ViewUtils.showHide(false, formSuccess);

			//populate avatar
			displayUserImage(ViewUtils.getAvatarImageUrl(currentUserBean.as().getName()));

			//title expires logic
			if (currentUserBean.as().getIsRDLSupporter()) {
				ViewUtils.showHide(false, supporterBtn);
				ViewUtils.showHide(true, titleExpiresParagraph);
				titleExpiresParagraph.setText(RDL.getI18n().userTitleExpires() + " " +
						ViewUtils.getTitleExpiryDate(currentUserBean, RDLConstants.UserTitle.RDL_SUPPORTER));
			} else {
				ViewUtils.showHide(false, titleExpiresParagraph);
				ViewUtils.showHide(true, supporterBtn);
			}
			viewPublicProfile.setHref("#" + RDLConstants.Tokens.PUBLIC_PROFILE + ":" + currentUserBean.as().getName());
		}
	}

	public void refreshImage() {
		displayUserImage(ViewUtils.getAvatarImageUrl(currentUserBean.as().getName()));
	}

	@Override
	public Widget asWidget() {
		displayUserImage(ViewUtils.getAvatarImageUrl(currentUserBean.as().getName()));
		return super.asWidget();
	}

	public void displayUserImage(String url) {
		avatarImage.setUrl(url + "?" + new Date().getTime());
	}

	/**
	 * shows the avatar upload widget
	 *
	 * @param e Standard GWT ClickEvent
	 */
	@UiHandler("profileImagePanel") void handleClick(ClickEvent e) {
		if (currentUserBean.as().getIsRDLSupporter()) {
			if (uploadForm == null) {
				uploadForm = new AvatarUploadPopUp(currentUserBean, this);
			}
			uploadForm.show();
		} else {
			GuiEventBus.EVENT_BUS
					.fireEvent(new BecomeRdlSupporterEvent(RDL.getI18n().rdlSupporterPopupTitleUploadImage()));
		}
	}

	@UiHandler("submitChangePassBtn") void handleSubmitNewPass(ClickEvent e) {
		String msg = presenter.changePassword(currentUserBean,
				oldPassword.getText(), newPassword.getText(), confirmNewPassword.getText());
		if (msg != null) {
			setChangePassErrorMsg(msg);
		}
	}

	@UiHandler("changePassBtn") void handleChangePass(ClickEvent e) {
		ViewUtils.showHide(false, formErrors);
		changePassModal.show();
	}

	@UiHandler("supporterBtn") void supporterBtnClick(ClickEvent e) {
		GuiEventBus.EVENT_BUS.fireEvent(new BecomeRdlSupporterEvent(RDL.getI18n().rdlSupporterPopupTitleDefault()));
	}

	@UiHandler("updateProfileDec") void updateProfileClicked(ClickEvent e) {
		presenter.updateProfile(profileDesc.getCode());
	}

	public void setFormSuccessMsg(String msg) {
		formSuccess.setSuccessMessage(msg);
		changePassModal.hide();
	}

	public void setChangePassErrorMsg(String msg) {
		if (!changePassModal.isVisible()) {
			changePassModal.show();
		}
		formErrors.setErrorMessage(msg);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void populateProfileDescription(String content) {
		profileDesc.setCode(content);
		profileDesc.reconfigure();
	}
}
