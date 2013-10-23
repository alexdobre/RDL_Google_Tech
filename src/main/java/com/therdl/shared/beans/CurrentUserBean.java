package com.therdl.shared.beans;

/**
 * See AuthUserBean javadoc for use
 * use this bean only to maintain client side state
 * temp solution until Internet Explorer is discontinued or improved
 * once IE7 is no longer used move to HTML5 session for success
 */
public interface CurrentUserBean {



     // current use bean needs the avatar url to be set in views where t is needed
    String getAvatarUrl();

    void setAvatarUrl(String avatarUrl);

    String getName() ;

    void setName(String name);

    String getEmail();

    void setEmail(String email);     ;

    boolean isAuth();

    void setAuth(boolean auth);

    boolean isRegistered();

    void setRegistered(boolean b);

}
