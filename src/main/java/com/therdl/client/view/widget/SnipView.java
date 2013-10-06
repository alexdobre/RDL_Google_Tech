package com.therdl.client.view.widget;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 *  snip view trigered when snip rowlist widget is selected
 */
public interface SnipView extends IsWidget {

    public interface Presenter {

    }

    void setPresenter(Presenter presenter);

    void setAppMenu(AutoBean<CurrentUserBean> currentUserBean);

}
