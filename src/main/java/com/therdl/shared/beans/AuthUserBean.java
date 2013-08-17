package com.therdl.shared.beans;

/**
 * A bean to store the current authorised user
 */
public interface  AuthUserBean {


    String getName() ;

    void setName(String name);

    String getEmail();

    void setEmail(String email);

    boolean isAuth();

    void setAuth(boolean auth);


}
