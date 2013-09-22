package com.therdl.shared.beans;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: serinekar
 * Date: 9/21/13
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SearchOptionsBean {

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
}
