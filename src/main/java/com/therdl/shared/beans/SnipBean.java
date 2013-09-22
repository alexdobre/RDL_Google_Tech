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

    String getTitle();
    void setTitle(String title);

    String getContent();
    void setContent(String content);

    String getAuthor();
    void setAuthor(String author);

    String getCreationDate();
    void setCreationDate(String creationDate);

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

    String getRep();
    void setRep(String rep);

    String getPosRef();
    void setPosRef(String posRef);

    String getNeutralRef();
    void setNeutralRef(String neutralRef);

    String getNegativeRef();
    void setNegativeRef(String negativeRef);

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
