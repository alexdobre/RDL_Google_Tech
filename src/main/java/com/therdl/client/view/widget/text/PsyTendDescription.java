package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the PsyTend core category of RDL
 * Created by Alex on 1/11/14.
 */
public class PsyTendDescription extends Composite {

    interface PsyTendDescriptionUiBinder extends UiBinder<HTMLPanel, PsyTendDescription> {
    }

    private static PsyTendDescriptionUiBinder uiBinder = GWT.create(PsyTendDescriptionUiBinder.class);

    public PsyTendDescription() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}