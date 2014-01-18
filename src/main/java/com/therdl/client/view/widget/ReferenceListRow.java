package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.widget.editor.RichTextToolbar;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
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
    Label rep, titleLabel, userName, creationDate, refFlag;

    public ReferenceListRow(AutoBean<SnipBean> referenceBean, AutoBean<CurrentUserBean> currentUserBean) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.referenceBean = referenceBean;

        // sets values from referenceBean for UI elements from ui binder
        richTextAreaRef.setHTML(referenceBean.as().getContent());
        richTextAreaRef.setEnabled(false);
        titleLabel.setText(RDL.i18n.userTitle()+" "+referenceBean.as().getAuthor());
        userName.setText(referenceBean.as().getAuthor());
        rep.setText(RDL.i18n.repLevel()+" "+referenceBean.as().getRep());
        creationDate.setText(referenceBean.as().getCreationDate().substring(0, referenceBean.as().getCreationDate().indexOf(" ")));

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
            if(referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.POSITIVE))
                refFlag.setText(RDL.i18n.positive());
            else if(referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEUTRAL))
                refFlag.setText(RDL.i18n.neutral());
            else if(referenceBean.as().getReferenceType().equals(RDLConstants.ReferenceType.NEGATIVE))
                refFlag.setText(RDL.i18n.negative());

            refFlag.getElement().getStyle().setProperty("backgroundColor", RDLConstants.ReferenceType.colorCodes.get(referenceBean.as().getReferenceType()));
        } else {
            refFlag.getElement().getStyle().setProperty("display","none");
        }
        // create badge table
        Grid badgeGrid = new Grid(4,2);
        badgeGrid.setCellPadding(2);
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 2; ++col) {
                Image img = new Image(Resources.INSTANCE.badgeImage().getSafeUri().asString());
                img.setWidth("27px");
                img.setHeight("30px");
                badgeGrid.setWidget(row, col, img);
            }
        }

        badgePanel.add(badgeGrid);
    }
}