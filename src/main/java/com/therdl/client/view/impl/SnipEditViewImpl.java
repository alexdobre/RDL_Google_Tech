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

public class SnipEditViewImpl<T> extends Composite implements SnipEditView<T> {

    private static Logger log = Logger.getLogger("");

	@UiTemplate("SnipEditViewImpl.ui.xml")
	interface SnipEditViewUiBinder extends UiBinder<Widget, SnipEditViewImpl> {}
	private static SnipEditViewUiBinder uiBinder = GWT.create(SnipEditViewUiBinder.class);
	 
	private Presenter<T> presenter;

	@UiField Widget appMenu;
	@UiField Widget leftMenuTree;
    @UiField FlowPanel mainPanel;

    private EditorViewHeader header;
    private SnipEditorWorkflow snipEditorWorkflow;
	
	public SnipEditViewImpl() {
	    initWidget(uiBinder.createAndBindUi(this));
	    appMenu =  WidgetHolder.getInstance().getAppMenu();
	    snipEditorWorkflow = new SnipEditorWorkflow();
		leftMenuTree = WidgetHolder.getInstance().getLeftMenuTree();
        header = new EditorViewHeader( snipEditorWorkflow);
        mainPanel.add(header);
        mainPanel.add(snipEditorWorkflow);



	  }

    @Override
    public void setSnipDropDown(List<AutoBean<SnipBean>> beans) {

        // set up header Dynamic Post List to store data as beans

        header.getPostListBox().addBeans(beans);
        beans.clear();

    }




	@Override
	public void setPresenter(Presenter<T> presenter) {
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



}
