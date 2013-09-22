package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.RegisterView;
import com.therdl.client.view.ServicesView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.*;

import java.util.logging.Logger;

/**
 *
 */
public class ServicesViewImpl extends Composite implements ServicesView {

    private static Logger log = Logger.getLogger("");
    private final  AutoBean<CurrentUserBean> currentUserBean;


    interface ServicesViewImplUiBinder extends UiBinder<Widget, ServicesViewImpl> {
    }

    private static ServicesViewImplUiBinder uiBinder = GWT.create(ServicesViewImplUiBinder.class);

    private ServicesView.Presenter presenter;

    private Beanery beanery = GWT.create(Beanery.class);

    @UiField
    HTMLPanel profile;

    @UiField
    FormPanel uploadForm;

    @UiField
    AppMenu appMenu;


    public ServicesViewImpl(final AutoBean<CurrentUserBean> cUserBean) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUserBean  =  cUserBean;
        setAppMenu(currentUserBean);
        setUploadForm();





        // user has just sucessfully logged in update app menu
        GuiEventBus.EVENT_BUS.addHandler(LogInOkEvent.TYPE, new LogInOkEventEventHandler()  {

            @Override
            public void onLogInOkEvent(LogInOkEvent onLoginOkEvent) {
                currentUserBean.as().setAuth(true);
                setAppMenu(currentUserBean);
            }
        });


    }


    @Override
    public void setAppMenu(AutoBean<CurrentUserBean> currentUserBean) {
        if (currentUserBean.as().isAuth()) {
            log.info("ServicesViewImpl setAppMenu auth true "+currentUserBean.as().getName() );
            profile.setVisible(true);
            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(currentUserBean.as().getName());
            this.appMenu.setEmail(currentUserBean.as().getEmail());
            this.appMenu.setLogInVisible(false);
        }

        else {
            profile.setVisible(false);
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

        HTML shtml = new HTML("AVATAR UPLOAD");
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

                Window.alert(event.getResults());
            }
        });

        uploadForm.setStyleName("uploadForm");

    }


}
