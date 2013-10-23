package com.therdl.server.api;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.mongodb.DB;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.UserBean;
import java.util.List;


/**
 * this is a service for User crud operations
 */
public interface UserService {

    /**
     * for testing the crud
     * drops the User collection
     */
    public void dropUserCollection();

    /**
     * testing and development methods debug string
     * @return
     */
    String getDebugString();
    UserBean getLastUser(String id);

    /**
     * gets the user Bean
     * @param id
     * @return
     */
    UserBean getUser(String id);

    /**
     * creates the new user
     * @param user
     */
    void createUser(UserBean user);

    /**
     * get all users (List<UserBean>)
     * @param id
     * @return
     */
    List<UserBean> getAllUsers();

    /**
     * deletes the user
     * @param id
     */
    void deleteUser(String id);

    /**
     * updates the user
     * @param user
     * @return
     */
    void updateUser(UserBean user);


    /**
     * finds a user on email
     * @param user
     * @return   UserBean
     */
    AutoBean<AuthUserBean> findUser(AuthUserBean bean, String hash);

}
