package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.EditorClientWidget;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;

public class SnipEditViewImpl  extends Composite implements SnipEditView  {

    private static Logger log = Logger.getLogger("");

	@UiTemplate("SnipEditViewImpl.ui.xml")
	interface SnipEditViewUiBinder extends UiBinder<Widget, SnipEditViewImpl> {}
	private static SnipEditViewUiBinder uiBinder = GWT.create(SnipEditViewUiBinder.class);
	 
	private Presenter presenter;

    @UiField
    AppMenu appMenu;

    @UiField
    DockLayoutPanel snipEditDocPanel;

    @UiField
    FlowPanel mainPanel;

    private  AutoBean<CurrentUserBean> currentUserBean;

	private EditorClientWidget editorClientWidget;

	public SnipEditViewImpl(AutoBean<CurrentUserBean> currentUserBean) {
        log.info("SnipEditViewImpl constructor");
	    initWidget(uiBinder.createAndBindUi(this));
        this.currentUserBean  =  currentUserBean;

        // init closure editor widget
        editorClientWidget = new EditorClientWidget(this);

        snipEditDocPanel.setSize("100%", "100%");
	}

    public void addEditorClientWidget(JSOModel snipData) {
        // add closure widget into the view
        mainPanel.add(editorClientWidget);
        if(snipData != null)
            editorClientWidget.bootStrapEditor(editorClientWidget, snipData);
        else
            editorClientWidget.bootStrapEditor(editorClientWidget);
    }

    // save
    @Override
    public void submitBean(AutoBean<SnipBean> bean) {

        presenter.submitBean(bean);

    }

    //update
    @Override
    public void submitEditBean(AutoBean<SnipBean> bean) {
        presenter.submitEditedBean(bean);
    }


    // delete
    @Override
    public void onDeleteSnip(String id) {
        presenter.onDeleteSnip(id);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }



    @Override
    public void onSaveButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveButtonClicked();
		}
	}

    @Override
    public void onCloseButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCloseButtonClicked();
		}
	}


    public EditorClientWidget getEditorClientWidget() {
        return editorClientWidget;
    }

    @Override
    public void setloginresult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true "+name );

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(name);
            this.appMenu.setEmail(email);
            this.appMenu.setLogInVisible(false);
        }

        else {
            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }

    }


    @Override
    public AppMenu getAppMenu() {
        return this.appMenu;
    }

}
