package com.therdl.client.view.widget.runtized;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.presenter.runt.ServiceFilterRunt;

/**
 * A couple of sort arrows
 */
public class SortBit extends Composite {
	interface SortBitUiBinder extends UiBinder<Widget, SortBit> {
	}

	private static SortBitUiBinder uiBinder = GWT.create(SortBitUiBinder.class);

	@UiField
	Anchor anchor;

	private boolean active;
	private ServiceFilterRunt runt;
	private int sortOrder;


	public SortBit() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * activates and deactivates the sort bit
	 *
	 * @param active is the sort bit active
	 */
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			anchor.removeStyleName("sortIconInActive");
			anchor.addStyleName("sortIconActive");
		} else {
			anchor.removeStyleName("sortIconActive");
			anchor.addStyleName("sortIconInActive");
		}
		displaySortOrder();
	}

	/**
	 * Changes the appearance of the bit to match the sort order
	 *
	 * @param sortOrder
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
		displaySortOrder();
	}

	private void displaySortOrder() {
		if (sortOrder == 1) {
			//ARROW down
			anchor.setIcon(IconType.ARROW_CIRCLE_DOWN);
		} else {
			//ARROW up
			anchor.setIcon(IconType.ARROW_CIRCLE_UP);
		}
	}

	/**
	 * handler for the create new button
	 * opens create/edit snip view
	 *
	 * @param event
	 */
	@UiHandler("anchor")
	void sortBitClicked(ClickEvent event) {
		runt.newSortAction(this);
	}

	/**
	 * is the current sort bit active
	 *
	 * @return true if the sort bit is active, false otherwise
	 */
	public boolean isActive() {
		return active;
	}

	public void setRunt(ServiceFilterRunt runt) {
		this.runt = runt;
	}
}
