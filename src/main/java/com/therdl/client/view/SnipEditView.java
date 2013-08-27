package com.therdl.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SnipEditorWorkflow;
import com.therdl.shared.beans.SnipBean;

import java.util.List;

public interface SnipEditView  {

	public interface Presenter {

		void onSaveButtonClicked();

		void onCloseButtonClicked();

        void onDeleteSnip(String id);

        void submitBean(AutoBean<SnipBean> bean );

        void submitEditedBean(AutoBean<SnipBean> bean);
	}

	void setPresenter(Presenter presenter);

    void onSaveButtonClicked(ClickEvent event);

    void onCloseButtonClicked(ClickEvent event);

    void setSnipDropDown(List<AutoBean<SnipBean>> beans);

    void clearSnipDropDown();

    void onDeleteSnip(String id);

    void submitBean(AutoBean<SnipBean> bean );

    void submitEditBean(AutoBean<SnipBean> bean);

    SnipEditorWorkflow getSnipEditorWorkflow();

    void setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    Widget asWidget();
}
