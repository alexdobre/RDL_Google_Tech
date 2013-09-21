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



    private void setUploadForm() {
        uploadForm.setAction("/avatarUpload");
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setMethod(FormPanel.METHOD_POST);
        // Create a panel to hold all of the form widgets.
        VerticalPanel panel = new VerticalPanel();
        HorizontalPanel hp = new HorizontalPanel();
        hp.setStyleName("formUplaodDataPanel");
        HTML shtml = new HTML("AVATAR UPLOAD");
        shtml.setStyleName("uploadHeader");
        panel.add(shtml);
        InlineLabel label = new InlineLabel();
        label.setText("image text");
        hp.add(label);
        uploadForm.setWidget(panel);

        // Create a TextBox, giving it a name so that it will be submitted.
        final TextBox tb = new TextBox();
        tb.setStyleName("textBoxFormElement");
        tb.setName("textBoxFormSubmit");

        hp.add(tb);
        panel.add(hp);

        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("uploadFormElement");
        panel.add(upload);

        // Add a 'submit' button.
        panel.add(new Button("Submit", new ClickHandler() {
            public void onClick(ClickEvent event) {
                uploadForm.submit();
            }
        }));

        // Add an event handler to the form.
        uploadForm.addSubmitHandler(new FormPanel.SubmitHandler() {
            public void onSubmit(FormPanel.SubmitEvent event) {
                // This event is fired just before the form is submitted. We can take
                // this opportunity to perform validation.
                if (tb.getText().length() == 0) {
                    Window.alert("The text box must not be empty");
                    event.cancel();
                }
            }
        });
        uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                // When the form submission is successfully completed, this event is
                // fired. Assuming the service returned a response of type text/html,
                // we can get the result text here (see the FormPanel documentation for
                // further explanation).
                Window.alert(event.getResults());
            }
        });

        uploadForm.setStyleName("uploadForm");

    }


}
