package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the Abuse core category of RDL
 * Created by Alex on 1/11/14.
 */
public class AbuseDescription extends Composite {

    interface AbuseDescriptionUiBinder extends UiBinder<HTMLPanel, AbuseDescription> {
    }
    private static AbuseDescriptionUiBinder uiBinder = GWT.create(AbuseDescriptionUiBinder.class);

    public AbuseDescription() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
