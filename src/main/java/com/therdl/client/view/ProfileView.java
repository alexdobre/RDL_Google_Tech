package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 *  see  ProfileViewImpl javadoc for these methods
 */
public interface ProfileView extends IsWidget {


    public interface Presenter {


    }

    void setPresenter(Presenter presenter);

    void setAppMenu(AutoBean<CurrentUserBean> currentUserBean);

    void setAvatarWhenViewIsNotNull();

}
