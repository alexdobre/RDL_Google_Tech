package com.therdl.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

import java.util.List;

public interface SnipEditView<T> {

	public interface Presenter<T> {

		void onSaveButtonClicked();

		void onCloseButtonClicked();
	}

	void setPresenter(Presenter<T> presenter);

    void onSaveButtonClicked(ClickEvent event);

    void onCloseButtonClicked(ClickEvent event);

    void setSnipDropDown(List<AutoBean<SnipBean>> beans);

    void clearSnipDropDown();

    Widget asWidget();
}
