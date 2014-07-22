package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the Seduction core category of RDL
 * Created by Alex on 1/11/14.
 */
public class SeductionDescription extends Composite {

    interface SeductionDescriptionUiBinder extends UiBinder<HTMLPanel, SeductionDescription> {
    }

    private static SeductionDescriptionUiBinder uiBinder = GWT.create(SeductionDescriptionUiBinder.class);

    public SeductionDescription() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}