package com.therdl.client.view.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.therdl.client.handler.ClickHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * This widget contains the edit/rep/repGiven buttons display
 */
public class SnipActionWidget extends Composite {

	private Button repBtn;
	private Button editBtn;
	private Icon repGiven;
	private Panel holder;


	public SnipActionWidget() {
		holder = new SimplePanel();
		initWidget(holder);
	}

	public void showRepBtn(final ClickHandler clickHandler) {
		holder.clear();
		//always create a new button to avoid click handler overlap
		repBtn = new Button();
		repBtn.setType(ButtonType.PRIMARY);
		repBtn.setIcon(IconType.THUMBS_UP);
		repBtn.addStyleName("repBtn");

		repBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clickHandler.onClick(event.getSource());
			}
		});
		holder.add(repBtn);
	}

	public void showEditBtn(final ClickHandler clickHandler) {
		holder.clear();
		//always create a new button to avoid click handler overlap
		editBtn = new Button();
		editBtn.setType(ButtonType.PRIMARY);
		editBtn.setIcon(IconType.PENCIL);
		editBtn.addStyleName("repBtn");

		editBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clickHandler.onClick(event.getSource());
			}
		});
		holder.add(editBtn);
	}

	public void showRepGiven() {
		holder.clear();
		if (repGiven == null) {
			repGiven = new Icon(IconType.CHECK);
			repGiven.setSize(IconSize.TIMES2);
			repGiven.addStyleName("repBtn");
			repGiven.addStyleName("repGivenIcon");
		}
		holder.add(repGiven);
	}

	public void hide() {
		holder.clear();
	}
}
