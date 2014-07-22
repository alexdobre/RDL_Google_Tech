package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the Affairs core category of RDL
 * Created by Alex on 1/11/14.
 */
public class AffairsDescription extends Composite {

    interface AffairsDescriptionUiBinder extends UiBinder<HTMLPanel, AffairsDescription> {
    }

    private static AffairsDescriptionUiBinder uiBinder = GWT.create(AffairsDescriptionUiBinder.class);

    public AffairsDescription() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
