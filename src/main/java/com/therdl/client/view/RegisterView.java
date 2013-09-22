package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;

/**
 * MVP requires these view interfaces
 *
 */
public interface RegisterView  extends IsWidget {


    public interface Presenter {

       void  submitNewUser(AutoBean<AuthUserBean> bean);


    }

    void setPresenter(Presenter presenter);






}
