package com.therdl.shared.beans;

/**
 * temp soulution untill Internet Explorer is discontinued or improved
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
