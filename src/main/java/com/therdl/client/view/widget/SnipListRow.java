package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * gwt widget for snip list row
 * constructor gets snipBean and logged userBean
 */
public class SnipListRow extends Composite{
    interface SnipListRowUiBinder extends UiBinder<HTMLPanel, SnipListRow> {
    }

    private static SnipListRowUiBinder ourUiBinder = GWT.create(SnipListRowUiBinder.class);

    AutoBean<SnipBean> snipBean;
    AutoBean<CurrentUserBean> currentUserBean;

    @UiField
    FlowPanel colorStripe, snipImgParent, secondColDiv;
    @UiField
    Label rep, titleLabel, snipTitle, userName, posRef, neutRef, negRef, viewCount;

    public SnipListRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.snipBean = snipBean;
        this.currentUserBean = currentUserBean;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        // sets background color for snip img and top color strip
        for(CoreCategory item : CoreCategory.values()) {
            if(item.getShortName().equals(snipBean.as().getCoreCat())) {
                colorStripe.getElement().getStyle().setProperty("backgroundColor", item.getColCode());
                snipImgParent.getElement().getStyle().setProperty("backgroundColor", item.getColCode());

            }
        }

        titleLabel.setText("User Title ("+snipBean.as().getAuthor()+")");
        userName.setText(snipBean.as().getAuthor());

        // set tooltip on the snip img and top color stripe

        String toolTip = snipBean.as().getTitle()+" / "+snipBean.as().getCoreCat();
        if(!snipBean.as().getSubCat().equals(""))
            toolTip += " / "+snipBean.as().getSubCat();

        // sets snip data
        snipImgParent.setTitle(toolTip);
        colorStripe.setTitle(toolTip);

        rep.setText(snipBean.as().getRep()+"");
        snipTitle.setText(snipBean.as().getTitle());
        posRef.setText(snipBean.as().getPosRef()+" Positive Ref");
        neutRef.setText(snipBean.as().getNeutralRef()+" Neutral Ref");
        negRef.setText(snipBean.as().getNegativeRef()+" Negative Ref");
        viewCount.setText(snipBean.as().getViews()+" Views");

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

        secondColDiv.add(badgeGrid);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
    }
}