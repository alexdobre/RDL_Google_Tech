package com.therdl.shared.beans;

/**
 * A bean to pass  the current authorised use;s credentials to the sever
 * for login and sign up
 *!!!! WARNING !!!!
 * this bean contains a user password
 * for client side security do not persist this bean at anytime
 * do not inject as singleton for obvious reasons
 * to maintain state use the current user bean
 * pass auth state to current user bean
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
