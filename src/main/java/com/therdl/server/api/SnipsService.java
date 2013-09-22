package com.therdl.server.api;


import com.therdl.shared.beans.SnipBean;

import java.util.List;

public interface SnipsService {

    /**
     * for testing the crud
     * drops the snip collection
     */
    public void dropSnipCollection();

    /**
     * testing and development methods debug string
     * @return
     */
    String getDebugString();
    SnipBean getLastSnip(String id);

    /**
     * gets the snip
     * @param id
     * @return
     */
    SnipBean getSnip(String id);

    /**
     * creates the new snip
     * @param snip
     */
    void createSnip(SnipBean snip);

    /**
     * get all snips for a user
     * @return
     */
    List<SnipBean> getAllSnips();


    /**
     * deletes the snip
     * @param id
     */
    void deleteSnip(String id);

    /**
     * updates the snip
     * @param snip
     * @return
     */
    void updateSnip(SnipBean snip);

    List<SnipBean> searchSnipsWith(SnipBean snip);
}
