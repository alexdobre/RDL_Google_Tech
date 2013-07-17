package com.therdl.shared.beans;

public interface SnipBean {

    String getTitle();
    void setTitle(String title);

    String getContentAsString();
    void setContentAsString(String content);

    String getContentAsHtml();
    void setContentAsHtml(String shtml);

    String getAuthor();
    void setAuthor(String author);

    String getTimeStamp();
    void setTimeStamp(String timeStamp);

    String getStream();
    void setStream(String stream);

    String getServerMessage();
    void setServerMessage(String stream);

    String getAction();
    void setAction(String action);


}
