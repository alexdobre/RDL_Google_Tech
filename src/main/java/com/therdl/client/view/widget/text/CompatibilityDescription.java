package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the Compatibility core category of RDL
 * Created by Alex on 1/11/14.
 */
public class CompatibilityDescription extends Composite {

    interface CompatibilityDescriptionUiBinder extends UiBinder<HTMLPanel, CompatibilityDescription> {
    }

    private static CompatibilityDescriptionUiBinder uiBinder = GWT.create(CompatibilityDescriptionUiBinder.class);

    public CompatibilityDescription() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
