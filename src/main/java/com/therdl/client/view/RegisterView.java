package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;

/**
 *  see  RegisterViewImpl javadoc for these methods
 */
public interface RegisterView  extends IsWidget {


    public interface Presenter {

       void  submitNewUser(AutoBean<AuthUserBean> bean);


    }

    void setPresenter(Presenter presenter);

    void setloginresult(String name, String email, boolean auth);

}
