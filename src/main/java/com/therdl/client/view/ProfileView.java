package com.therdl.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * see com.therdl.client.view.impl.ProfileViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ setAppMenu(AutoBean<CurrentUserBean> currentUserBean) using the user authorisation status
 * this method sets the options in the header/nav bar
 * @ setAvatarWhenViewIsNotNull( ) sets the avatar image when the profile view comes into focus
 */
public interface ProfileView extends IsWidget {


    public interface Presenter {


    }

    void setPresenter(Presenter presenter);

    void setAppMenu(AutoBean<CurrentUserBean> currentUserBean);

    void setAvatarWhenViewIsNotNull();

}
