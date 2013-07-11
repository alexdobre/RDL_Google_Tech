package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class SnipListWidget extends Composite {

	private static SnipListWidgetUiBinder uiBinder = GWT
			.create(SnipListWidgetUiBinder.class);
	@UiField(provided=true) CellList<Object> snipCellList = new CellList<Object>(new AbstractCell<Object>(){
		@Override
		public void render(Context context, Object value, SafeHtmlBuilder sb) {
			// TODO
		}
	});

	interface SnipListWidgetUiBinder extends UiBinder<Widget, SnipListWidget> {
	}

	public SnipListWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
