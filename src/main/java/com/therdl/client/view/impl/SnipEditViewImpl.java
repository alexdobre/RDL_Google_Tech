package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SnipEditorWorkflow;
import com.therdl.client.view.widget.WidgetHolder;
import com.therdl.client.view.widget.editor.EditorViewHeader;
import com.therdl.shared.beans.SnipBean;

import java.util.List;
import java.util.logging.Logger;

public class SnipEditViewImpl  extends Composite implements SnipEditView  {

    private static Logger log = Logger.getLogger("");

	@UiTemplate("SnipEditViewImpl.ui.xml")
	interface SnipEditViewUiBinder extends UiBinder<Widget, SnipEditViewImpl> {}
	private static SnipEditViewUiBinder uiBinder = GWT.create(SnipEditViewUiBinder.class);
	 
	private Presenter presenter;


    private AppMenu appMenuPanel;
    @UiField HTMLPanel appMenu;
	@UiField Widget leftMenuTree;
    @UiField FlowPanel mainPanel;

    private EditorViewHeader header;


    private SnipEditorWorkflow snipEditorWorkflow;
	
	public SnipEditViewImpl() {
	    initWidget(uiBinder.createAndBindUi(this));
        appMenuPanel = (AppMenu) WidgetHolder.getInstance().getAppMenu();
        appMenu.add(appMenuPanel);
	    snipEditorWorkflow = new SnipEditorWorkflow(this);
		leftMenuTree = WidgetHolder.getInstance().getLeftMenuTree();
        header = new EditorViewHeader( snipEditorWorkflow);
        mainPanel.add(header);
        mainPanel.add(snipEditorWorkflow);

	  }

    @Override
    public void setSnipDropDown(List<AutoBean<SnipBean>> beans) {

        // set up header Dynamic Post List to store data as beans
        log.info("SnipEditViewImplonResponseReceived setSnipDropDown " + beans.size());
        header.getPostListBox().addBeans(beans);
      //  beans.clear();

    }

    @Override
    public void clearSnipDropDown() {

        // set up header Dynamic Post List to store data as beans
        log.info("SnipEditViewImplonResponseReceived clearSnipDropDown ");
        header.getPostListBox().clear();
        //  beans.clear();

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


    public SnipEditorWorkflow getSnipEditorWorkflow() {
        return snipEditorWorkflow;
    }



    @Override
    public void setloginresult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true "+name );

            this.appMenuPanel.setLogOutVisible(true);
            this.appMenuPanel.setSignUpVisible(false);
            this.appMenuPanel.setUserInfoVisible(true);
            this.appMenuPanel.setUser(name);
            this.appMenuPanel.setEmail(email);
        }

    }

    @Override
    public AppMenu getAppMenu() {
        return this.appMenuPanel;
    }

}
