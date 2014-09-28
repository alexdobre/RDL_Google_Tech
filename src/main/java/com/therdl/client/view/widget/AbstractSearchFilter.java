package com.therdl.client.view.widget;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.TextBox;

/**
 * Common view items for search filter
 */
public class AbstractSearchFilter extends Composite {

	@UiField
	public TextBox title;

	@UiField
	public DateFilterWidget dateFilterWidget;
}
