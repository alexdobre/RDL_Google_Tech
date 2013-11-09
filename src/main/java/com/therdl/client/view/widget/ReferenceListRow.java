package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * gwt widget class for reference row in snip view page
 */
public class ReferenceListRow extends Composite{
    interface ReferenceListRowUiBinder extends UiBinder<HTMLPanel, ReferenceListRow> {
    }

    private static ReferenceListRowUiBinder ourUiBinder = GWT.create(ReferenceListRowUiBinder.class);

    private AutoBean<SnipBean> referenceBean;

    @UiField
    FlowPanel rightPanel, badgePanel;

    @UiField
    RichTextArea richTextAreaRef;

    @UiField
    Label rep, titleLabel, userName, creationDate;

    public ReferenceListRow(AutoBean<SnipBean> referenceBean, AutoBean<CurrentUserBean> currentUserBean) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.referenceBean = referenceBean;

        // sets values from referenceBean for UI elements from ui binder
        richTextAreaRef.setHTML(referenceBean.as().getContent());
        titleLabel.setText("User Title ("+referenceBean.as().getAuthor()+")");
        userName.setText(referenceBean.as().getAuthor());
        rep.setText(referenceBean.as().getRep()+" Rep Level");
        creationDate.setText(referenceBean.as().getCreationDate().substring(0, referenceBean.as().getCreationDate().indexOf(" ")));

        // create badge table
        Grid badgeGrid = new Grid(3,3);
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                Image img = new Image(Resources.INSTANCE.badgeImage().getSafeUri().asString());
                img.setWidth("27px");
                img.setHeight("30px");
                badgeGrid.setWidget(row, col, img);
            }
        }

        badgePanel.add(badgeGrid);
    }
}