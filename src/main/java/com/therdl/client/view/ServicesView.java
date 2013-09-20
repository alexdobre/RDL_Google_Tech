package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;

/**
 *
 */
public interface ServicesView  extends IsWidget {


    public interface Presenter {



    }

    void setPresenter(Presenter presenter);

}
