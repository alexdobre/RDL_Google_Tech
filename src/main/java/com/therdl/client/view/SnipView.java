package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 *  snip view triggered when snip row list widget is selected
 */
public interface SnipView extends IsWidget {

    public interface Presenter {
        public void saveReference(AutoBean<SnipBean> bean);
        public void getSnipReferences(String referenceTypes);
    }

    void setPresenter(Presenter presenter);

    void setAppMenu(AutoBean<CurrentUserBean> currentUserBean);

    public void viewSnip(AutoBean<SnipBean> snipBean);

    public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, String referenceTypes);

}
