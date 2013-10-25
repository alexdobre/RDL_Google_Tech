package com.therdl.shared.beans;

/**
 * See AuthUserBean javadoc for use
 * use this bean only to maintain client side state
 * temp solution until Internet Explorer is discontinued or improved
 * once IE7 is no longer used move to HTML5 session for success
 */
public interface CurrentUserBean {


    /**
     * AuthUserBean needs the avatar url to pass to currentuserbean
     */
    String getAvatarUrl();

    /**
     * AuthUserBean needs the avatar url to pass to currentuserbean
     *
     * @ String avatarUrl  the uri to locate the users image,
     * used in the browser/javascript layer
     */
    void setAvatarUrl(String avatarUrl);

    /**
     * methods below are for standard form based credentials submitted on the clien
     * for user login and sign up
     *
     * @return
     */
    String getName();

    void setName(String name);

    String getEmail();

    void setEmail(String email);

    ;

    /**
     * this method is used to encapsulate the users authorisation status
     * goal is to minimise password transport
     *
     * @return
     */
    boolean isAuth();

    /**
     * this method is used to encapsulate the users authorisation status
     * goal is to minimise password transport
     * boolean auth set after login and sign up, true is logged in
     *
     * @return
     */
    void setAuth(boolean auth);

    /**
     * this method is used to encapsulate the users authorisation status
     * after a registration event
     * goal is to minimise password transport
     *
     * @return
     */
    boolean isRegistered();

    /**
     * this method is used to encapsulate the users authorisation status
     * goal is to minimise password transport
     * boolean b set after sign up, true is signn up success
     *
     * @return
     */
    void setRegistered(boolean b);

}
