package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.AvatarUploadPopUp;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.events.*;

import java.util.logging.Logger;


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
 * @ AppMenu appMenu the upper menu view
 * @ FocusPanel profileImagePanel   GWT FocusPanel allows events for a image container
 * see http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/FocusPanel.html
 * @ Image pic  the users avatar image
 * @ String tempUrl displays a empty avatar image to indicate where a user Avatar would appear if it was uploded,
 * also to prompt the uses to click to initiate the upload
 */
public class ProfileViewImpl extends Composite implements ProfileView {

    private static Logger log = Logger.getLogger("");

    private final AutoBean<CurrentUserBean> currentUserBean;

    interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl> {
    }

    private static ProfileViewImplUiBinder uiBinder = GWT.create(ProfileViewImplUiBinder.class);

    private ProfileView.Presenter presenter;


    private AvatarUploadPopUp uploadForm;

    @UiField
    AppMenu appMenu;

    @UiField
    FocusPanel profileImagePanel;

    @UiField
    HTMLPanel titlePanel;

    private Image pic;

    /**
     * URI for the empty pace holder image, used when user has not yet uploaded a avatar image
     */
    private String tempUrl = "userAvatar/avatar-empty.jpg";

    public ProfileViewImpl(final AutoBean<CurrentUserBean> cUserBean) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUserBean = cUserBean;
        setAppMenu(currentUserBean);

        profileImagePanel.setStyleName("profileImagePanel");

        // check if user has an avatar set to default
        log.info("ProfileViewImpl check if user has an avatar ");
        if (currentUserBean.as().getAvatarUrl() == null) {
            log.info("ProfileViewImpl getAvatarUrl()== null: ");
            // can set the parameters for the avatar place holder here

            pic = new Image(tempUrl);
            profileImagePanel.clear();
            profileImagePanel.add(pic);
            profileImagePanel.setVisible(true);
            pic.setStyleName("profileImage");
            pic.setVisible(true);
            if (uploadForm != null) {
                uploadForm.clear();
                uploadForm.hide();
            }

        } else {

            setAvatar(currentUserBean.as().getAvatarUrl());
        }

        for (UserBean.TitleBean titleBean : cUserBean.as().getTitles()){
            titlePanel.add(new HTMLPanel("<p>Title: "+titleBean.getTitleName()+" dateGained: "+ titleBean.getDateGained()+" dateExpires: "+titleBean.getExpires()));
        }


        // user has just sucessfully logged in update app menu
        GuiEventBus.EVENT_BUS.addHandler(LogInOkEvent.TYPE, new LogInOkEventEventHandler() {

            @Override
            public void onLogInOkEvent(LogInOkEvent onLoginOkEvent) {
                currentUserBean.as().setAuth(true);
                setAppMenu(currentUserBean);
            }
        });

    }


    /**
     * sets the Avatar for a User when a valid  avatar url exists in the currentUserBean
     */
    @Override
    public void setAvatarWhenViewIsNotNull() {
        log.info("ProfileViewImpl setAvatarWhenViewIsNotNull" + currentUserBean.as().getAvatarUrl());


        if (currentUserBean.as().getAvatarUrl() == null) {
            log.info("ProfileViewImpl getAvatarUrl()== null: ");
            // can set the parameters for the avatar place holder here

            pic = new Image(tempUrl);
            profileImagePanel.clear();
            profileImagePanel.add(pic);
            profileImagePanel.setVisible(true);
            pic.setStyleName("profileImage");
            pic.setVisible(true);
            if (uploadForm != null) {
                uploadForm.clear();
                uploadForm.hide();
            }

        } else {

            setAvatar(currentUserBean.as().getAvatarUrl());
        }


    }


    /**
     * shows the avatar upload widget
     *
     * @param e Standard GWT ClickEvent
     */
    @UiHandler("profileImagePanel")
    void handleClick(ClickEvent e) {
        showUploadPopUp();
    }


    /**
     * as above public as may need to be called as a refresh at some point
     */
    public void showUploadPopUp() {

        uploadForm = new AvatarUploadPopUp(this);
        uploadForm.setGlassEnabled(false);
        uploadForm.setModal(true);
        uploadForm.setPopupPosition(20, 30);
        uploadForm.show();
        profileImagePanel.setVisible(false);
        uploadForm.setStyleName("uploadPopUp");

    }

    /**
     * Sets the upper header Menu to the correct state for a given users auth state(eg logged in)
     *
     * @param AutoBean currentUserBean
     */

    @Override
    public void setAppMenu(AutoBean<CurrentUserBean> currentUserBean) {
        if (currentUserBean.as().isAuth()) {
            log.info("ProfileViewImpl setAppMenu auth true " + currentUserBean.as().getName());

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(currentUserBean.as().getName());
            this.appMenu.setEmail(currentUserBean.as().getEmail());
            this.appMenu.setLogInVisible(false);
        } else {

            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }

    }


    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * sets the User Avatar Image if one exists
     *
     * @param String url  URI for the user image in the browser resources
     */
    public void setAvatar(String url) {
        log.info("ProfileViewImpl setAvatarWhenViewIsNotNull" + currentUserBean.as().getAvatarUrl());
        pic = new Image(url);
        profileImagePanel.clear();
        profileImagePanel.add(pic);
        profileImagePanel.setVisible(true);
        pic.setStyleName("profileImage");
        pic.setVisible(true);
        if (uploadForm != null) {
            uploadForm.clear();
            uploadForm.hide();
        }
    }

    public FocusPanel getProfileImagePanel() {
        return profileImagePanel;
    }


}
