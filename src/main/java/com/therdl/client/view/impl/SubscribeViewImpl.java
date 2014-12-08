package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.SubscribeView;
import com.therdl.client.view.widget.AppMenu;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.PanelBody;

/**
 * The subscribe view
 *
 * @author alex
 */
public class SubscribeViewImpl extends AppMenuView implements SubscribeView {

	private SubscribeView.Presenter presenter;
	private boolean populated = false;

	interface SubscribeViewImplUiBinder extends UiBinder<Widget, SubscribeViewImpl> {
	}

	private static SubscribeViewImplUiBinder uiBinder = GWT.create(SubscribeViewImplUiBinder.class);

	@UiField
	PanelBody descBody;
	@UiField
	Column footer;

	public SubscribeViewImpl(AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(SubscribeView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public boolean isPopulated() {
		return populated;
	}

	@Override
	public void setPopulated(boolean populated) {
		this.populated = populated;
	}

	@Override
	public Column getFooter() {
		return footer;
	}

	@Override
	public PanelBody getDescBody() {
		return descBody;
	}
}