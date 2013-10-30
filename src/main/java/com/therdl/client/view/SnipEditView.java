package com.therdl.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.EditorClientWidget;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.List;

public interface SnipEditView  {

	public interface Presenter {

        void onDeleteSnip(String id);

        void submitBean(AutoBean<SnipBean> bean );

        void submitEditedBean(AutoBean<SnipBean> bean);
	}

	void setPresenter(Presenter presenter);

    void onDeleteSnip(String id);

    void submitBean(AutoBean<SnipBean> bean );

    void submitEditBean(AutoBean<SnipBean> bean);

    public void addEditorClientWidget(JSOModel snipData);

    EditorClientWidget getEditorClientWidget();

    void setloginresult(String name, String email, boolean auth);

    AppMenu getAppMenu();

    Widget asWidget();
}
