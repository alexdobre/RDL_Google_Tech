package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.view.impl.ProfileViewImpl;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;

import java.util.logging.Logger;

/**
 * this class will handle the file upload as a pop up
 * triggered by clicking on the avatar image place holder
 *
 */
public class AvatarUploadPopUp extends PopupPanel {

    private static Logger sLogger = Logger.getLogger("");

    interface AvatarUploadPopUpUiBinder extends UiBinder<Widget, AvatarUploadPopUp> {
    }

    private static AvatarUploadPopUpUiBinder uiBinder = GWT.create(AvatarUploadPopUpUiBinder.class);

    @UiField
    HTMLPanel profileUpLoadFormPanel;

    @UiField
    FormPanel uploadForm;

    // to handle data view and events for this pop up
    ProfileViewImpl mainView;

    private Beanery beanery = GWT.create(Beanery.class);

    public AvatarUploadPopUp(ProfileViewImpl mainView) {
        super(true);
        this.mainView = mainView;
        add(uiBinder.createAndBindUi(this));
        setUploadForm();
        profileUpLoadFormPanel.setStyleName("profileUpLoadFormPanel");

        sLogger .info("AvatarUploadPopUp");
    }

    // pleae note as we are using the gwt form upload widget we break the mvp here in a small way only
    private void setUploadForm() {
        String uploadUrl =GWT.getModuleBaseURL()+"avatarUpload";
        if(!Constants.DEPLOY){
            uploadUrl = uploadUrl.replaceAll("/therdl", "");
        }
        uploadForm.setAction(uploadUrl);
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setMethod(FormPanel.METHOD_POST);
        // Create a panel to hold all of the form widgets.
        VerticalPanel panel = new VerticalPanel();

        HTML shtml = new HTML("AVATAR UPLOAD only jpeg image 200*200px supported");
        shtml.setStyleName("uploadHeader");
        panel.add(shtml);

        uploadForm.setWidget(panel);


        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("fileElement");
        upload.setStylePrimaryName("uploadFormElement");
        panel.add(upload);

        Button submit =new Button("Submit", new ClickHandler() {
            public void onClick(ClickEvent event) {
                uploadForm.submit();
            }
        });
        submit.setStylePrimaryName("uploadSubmit");

        // Add a 'submit' button.
        panel.add(submit);

        // Add an event handler to the form.
        uploadForm.addSubmitHandler(new FormPanel.SubmitHandler() {
            public void onSubmit(FormPanel.SubmitEvent event) {

            }
        });
        uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {

                // rawResult =<pre style="word-wrap: break-word; white-space: pre-wrap;">{"action":"ok"}</pre>
                String rawResult = event.getResults();
                String jsonResult = rawResult.substring(rawResult.indexOf("{"), rawResult.indexOf("}")+1);

                sLogger.info("ProfileViewImpl addSubmitCompleteHandler parsed resutls"+ jsonResult);

                AutoBean<AuthUserBean> bean = AutoBeanCodex.decode(beanery, AuthUserBean.class, jsonResult);

                sLogger.info("ProfileViewImpl addSubmitCompleteHandlerbean.as().getAction()"+ bean.as().getAction());
                if (bean.as().getAction().equals("ok")) {

                    mainView.setAvatar(bean.as().getAvatarUrl());
                    // go to the home page for now so refresh event is initiated
                    // GuiEventBus.EVENT_BUS.fireEvent(new RefreshEvent());
                }   else  Window.alert(bean.as().getAction());
            }
        });

        uploadForm.setStyleName("uploadForm");
    }



    @Override
    protected void onDetach() {
        mainView.getProfileImagePanel().setVisible(true);

    }


    public HTMLPanel getProfileUpLoadFormPanel() {
        return profileUpLoadFormPanel;
    }



}