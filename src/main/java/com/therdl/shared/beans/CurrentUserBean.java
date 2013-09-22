package com.therdl.shared.beans;

/**
 * temp soulution untill Internet Explorer is discontinued or improved
 */
public interface CurrentUserBean {
    String getName() ;

    void setName(String name);

    String getEmail();

    void setEmail(String email);     ;

    boolean isAuth();

    void setAuth(boolean auth);

    boolean isRegistered();

    void setRegistered(boolean b);

}
