package com.therdl.client.view.widget.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This widget contains the text description of the Eroticism core category of RDL
 * Created by Alex on 1/11/14.
 */
public class EroticismDescription extends Composite {

    interface EroticismDescriptionUiBinder extends UiBinder<HTMLPanel, EroticismDescription> {
    }
    private static EroticismDescriptionUiBinder uiBinder = GWT.create(EroticismDescriptionUiBinder.class);

    public EroticismDescription() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}