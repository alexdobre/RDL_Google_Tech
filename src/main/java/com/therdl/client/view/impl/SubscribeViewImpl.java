package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.SubscribeView;
import com.therdl.client.view.widget.AppMenu;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelFooter;
import org.gwtbootstrap3.client.ui.RadioButton;

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

	@UiField
	Anchor ancSupporter;

	@UiField
	RadioButton radButUsd;
	@UiField
	RadioButton radButGbp;
	@UiField
	RadioButton radButEur;

	@UiField
	PanelBody subscribeFooter;

	public SubscribeViewImpl(AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));

		radButUsd.setActive(true);
		ancSupporter.setHref("/rdl/paypalCheckout?cur=USD");
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

	@Override
	public PanelBody getSubscribeFooter() {
		return subscribeFooter;
	}

	@UiHandler("radButUsd")
	void handleRadButUsd(ClickEvent e) {
		radButUsd.setActive(true);
		radButGbp.setActive(false);
		radButEur.setActive(false);

		ancSupporter.setHref("/rdl/paypalCheckout?cur=USD");
	}

	@UiHandler("radButGbp")
	void handleRadButGbp(ClickEvent e) {
		radButUsd.setActive(false);
		radButGbp.setActive(true);
		radButEur.setActive(false);
		ancSupporter.setHref("/rdl/paypalCheckout?cur=GBP");
	}

	@UiHandler("radButEur")
	void handleRadButEur(ClickEvent e) {
		radButUsd.setActive(false);
		radButGbp.setActive(false);
		radButEur.setActive(true);
		ancSupporter.setHref("/rdl/paypalCheckout?cur=EUR");
	}
}