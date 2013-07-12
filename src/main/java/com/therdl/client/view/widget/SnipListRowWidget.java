package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiField;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;

public class SnipListRowWidget extends Composite {

    private static SnipListRowWidgetUiBinder uiBinder = GWT.create(SnipListRowWidgetUiBinder.class);

    private static Logger log = Logger.getLogger("");

    @UiField
    SpanElement title;
    @UiField
    SpanElement servermessage;
    @UiField
    SpanElement content;
    @UiField
    SpanElement timeStamp;
    @UiField
    SpanElement author;
    @UiField
    SpanElement stream;

    AutoBean<SnipBean> bean;

    interface SnipListRowWidgetUiBinder extends
            UiBinder<Widget, SnipListRowWidget> {
    }

    public SnipListRowWidget(AutoBean<SnipBean> bean) {
        this.bean = bean;
        initWidget(uiBinder.createAndBindUi(this));
        title.setInnerText(bean.as().getTitle());
        servermessage.setInnerText("rdl server message: "+bean.as().getServerMessage());
        content.setInnerText("  Content: "+bean.as().getContent());
        timeStamp.setInnerText("Date-Time: "+bean.as().getTimeStamp());
        author.setInnerText("Author: "+bean.as().getAuthor());
        stream.setInnerText("Stream: "+bean.as().getStream());
    }






}
