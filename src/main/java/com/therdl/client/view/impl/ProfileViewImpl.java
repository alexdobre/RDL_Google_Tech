package com.therdl.client.view.impl;

import java.util.Date;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormControlStatic;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.base.AbstractTextWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.AvatarUploadPopUp;
import com.therdl.client.view.widget.TitleListRow;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.UserBean;


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
public class ProfileViewImpl extends AppMenuView implements ProfileView {

	private AutoBean<CurrentUserBean> currentUserBean;

	interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl> {
	}

	private static ProfileViewImplUiBinder uiBinder = GWT.create(ProfileViewImplUiBinder.class);

	private ProfileView.Presenter presenter;


	private AvatarUploadPopUp uploadForm;

	@UiField
	FocusPanel profileImagePanel;

	@UiField
	HTMLPanel titlePanel;

	@UiField
	Anchor ancBeAMember;

	@UiField
	FormControlStatic email, username;

	@UiField
	Button changePassBtn;

	@UiField
	Image avatarImage;

	public ProfileViewImpl(AutoBean<CurrentUserBean> cUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = cUserBean;

		populateView(currentUserBean);
	}

	public void populateView(AutoBean<CurrentUserBean> currentUserBean) {
		if (currentUserBean != null && currentUserBean.as() != null) {
			this.currentUserBean = currentUserBean;
			email.setText(currentUserBean.as().getEmail());
			username.setText(currentUserBean.as().getName());
			ancBeAMember.setVisible(true);

			//populate avatar
			displayUserImage(getImageUrl());
			//populate titles
			for (UserBean.TitleBean titleBean : currentUserBean.as().getTitles()) {
				titlePanel.add(new TitleListRow(currentUserBean, titleBean).asWidget());

				if (titleBean.getTitleName().equals(RDLConstants.UserTitle.RDL_SUPPORTER)) {
					ancBeAMember.setVisible(false);
				}
			}
		}
	}

	public void refreshImage() {
		displayUserImage(getImageUrl());
	}

	@Override
	public Widget asWidget() {
		displayUserImage(getImageUrl());
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
	@UiHandler("profileImagePanel")
	void handleClick(ClickEvent e) {
		if (uploadForm == null) {
			uploadForm = new AvatarUploadPopUp(currentUserBean, this);
		}
		uploadForm.show();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private String getImageUrl() {
		return "https://s3.amazonaws.com/RDL_Avatars/" +
				ViewUtils.createAvatarName(currentUserBean.as().getName()) + ".jpg";
	}
}
