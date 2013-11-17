package com.therdl.server.api;


import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

import java.util.List;


/**
 * this is a service for Snip crud operations
 */
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
     * @return returns id of the inserted record
     */
    String createSnip(SnipBean snip);

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

    /**
     * increments counter for the given snip id
     * @param id
     * @param field to increment. This can be viewCount, rep or positive/neutral/negative reference count
     * @return
     */
    SnipBean incrementCounter(String id, String field);

    /**
     * search snips for the given search options
     * @param snip search option data
     * @return list of SnipBean
     */
    List<SnipBean> searchSnipsWith(SnipBean snip);

    /**
     * makes current timestamp
     * @return current timestamp as String
     */
    public String makeTimeStamp();

    /**
     * snip json contains an array of link objects representing references for the current snip
     * adds a reference to that array for the snip with the given snip id
     * @param linkAutoBean Link bean object
     * @param parentSnipId parent snip id
     * @return parent modified SnipBean
     */
    SnipBean addReference(AutoBean<SnipBean.Link> linkAutoBean, String parentSnipId);

    /**
     * finds references of the snip with the given id
     * @param id snip id
     * @param referenceType filter by reference type, could be more than 1 reference type
     * @return references as a list of SnipBean object
     */
    public List<SnipBean> getReferences(String id, String referenceType);
}
