package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.WidgetHolder;

import java.util.logging.Logger;

public class SnipEditViewImpl<T> extends Composite implements SnipEditView<T> {

    private static Logger log = Logger.getLogger("");

	@UiTemplate("SnipEditViewImpl.ui.xml")
	interface SnipEditViewUiBinder extends UiBinder<Widget, SnipEditViewImpl> {}
	private static SnipEditViewUiBinder uiBinder = GWT.create(SnipEditViewUiBinder.class);
	 
	private Presenter<T> presenter;

	@UiField Widget appMenu;
	@UiField Widget snipEditorWorkflow;
	@UiField Widget leftMenuTree;
	
	public SnipEditViewImpl() {
	    initWidget(uiBinder.createAndBindUi(this));
	    appMenu =  WidgetHolder.getInstance().getAppMenu();
	    snipEditorWorkflow = WidgetHolder.getInstance().getSnipSearchWidget();
		leftMenuTree = WidgetHolder.getInstance().getLeftMenuTree();
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
