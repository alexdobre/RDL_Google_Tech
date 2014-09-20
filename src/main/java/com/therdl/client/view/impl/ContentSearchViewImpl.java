package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.ContentSearchView;
import com.therdl.client.view.widget.AppMenu;
import org.gwtbootstrap3.client.ui.Column;

import java.util.logging.Logger;

/**
 * The user searches for site content via google custom search
 */
public class ContentSearchViewImpl extends AppMenuView implements ContentSearchView {

	private static Logger log = Logger.getLogger(ContentSearchViewImpl.class.getName());

	interface ContentSearchViewImplUiBinder extends UiBinder<Widget, ContentSearchViewImpl> {
	}

	private static ContentSearchViewImplUiBinder uiBinder = GWT.create(ContentSearchViewImplUiBinder.class);

	private ContentSearchView.Presenter presenter;

	@UiField
	Column searchResults;

	public ContentSearchViewImpl(AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(ContentSearchView.Presenter presenter) {
		this.presenter = presenter;
	}

	public Panel getResultsContainer() {
		return searchResults;
	}

}