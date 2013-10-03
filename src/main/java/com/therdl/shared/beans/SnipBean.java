package com.therdl.shared.beans;

import java.util.List;

public interface SnipBean {

    /**
     * this field is not being saved in the db
     * it is mainly the action for the server to handle the request
     * @return
     */
    String getAction();
    void setAction(String action);

    /**************************** Getters and Setters ******************************/

    String getId();
    void setId(String id);

    String getAvatarUrl();
    void setAvatarUrl(String avatarUrl);

    String getTitle();
    void setTitle(String title);

    String getContent();
    void setContent(String content);

    String getAuthor();
    void setAuthor(String author);

    String getCreationDate();
    void setCreationDate(String creationDate);

    String getDateFrom();
    void setDateFrom(String dateFrom);

    String getDateTo();
    void setDateTo(String dateTo);


    String getEditDate();
    void setEditDate(String editDate);

    String getSnipType();
    void setSnipType(String snipType);

    String getCoreCat();
    void setCoreCat(String coreCat);

    String getSubCat();
    void setSubCat(String subCat);

    String getViews();
    void setViews(String views);

    Integer getRep();
    void setRep(Integer rep);

    Integer getPosRef();
    void setPosRef(Integer posRef);

    Integer getNeutralRef();
    void setNeutralRef(Integer neutralRef);

    Integer getNegativeRef();
    void setNegativeRef(Integer negativeRef);

    String getReferenceType();
    void setReferenceType(String referenceType);

    List<Links> getLinks();
    void setLinks(List<Links> links);

    String getParentStream();
    void setParentStream(String parentStream);

    String getParentTag();
    void setParentTag(String parentTag);

    String getParentThread();
    void setParentThread(String parentThread);

    String getVotes();
    void setVotes(String votes);

    String getMoney();
    void setMoney(String money);

    /**
     * nested interfaces
     */
    interface Links {
        String getTargetId();
        void setTargetId(String targetId);

        String getRank();
        void setRank(String rank);
    }
}
