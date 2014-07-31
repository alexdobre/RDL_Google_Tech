package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.shared.RequestObserver;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * snip view triggered when snip row list widget is selected
 */
public interface SnipView extends IsWidget {

    public interface Presenter {
        public void saveReference(AutoBean<SnipBean> bean);

        public void getSnipReferences(AutoBean<SnipBean> searchOptionsBean, final int pageIndex);

        public void giveSnipReputation(String id, final RequestObserver observer);

        public AppController getController();

    }

    void setPresenter(Presenter presenter);

    public Presenter getPresenter();

    void setAppMenu(AutoBean<CurrentUserBean> currentUserBean);

    public void viewSnip(AutoBean<SnipBean> snipBean);

    public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex, String listRange);

    public void getSnipReferences(AutoBean<SnipBean> searchOptions);

    public void giveRepResponseHandler();

    public void saveReferenceResponseHandler(String refType, String snipType);

}
