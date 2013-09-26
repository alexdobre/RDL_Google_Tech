package com.therdl.shared.beans;

/**
 * A bean to store the current authorised user
 */
public interface  AuthUserBean {

    // AuthUserBean needs the avatar url to pass to currentuserbean
    String getAvatarUrl();

    void setAvatarUrl(String avatarUrl);

    String getName() ;

    void setName(String name);

    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);       ;

    boolean isAuth();

    void setAuth(boolean auth);

    String getAction();

    void setAction(String action);


}
