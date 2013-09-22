package com.therdl.shared.beans;

public interface SearchOptionsBean {

    String getTitle();
    void setTitle(String title);

    int getPageSize();
    void setPageSize(int pageSize);

    String getContent();
    void setContent(String content);

    String getAuthor();
    void setAuthor(String author);

    String getDateFrom();
    void setDateFrom(String dateFrom);

    String getDateTo();
    void setDateTo(String dateTo);

    String getCoreCat();
    void setCoreCat(String coreCat);
}
