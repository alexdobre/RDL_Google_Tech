package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.SnipViewEvent;

import java.util.Date;
import java.util.logging.Logger;

/**
 * gwt widget for snip list row
 * constructor gets snipBean and logged userBean
 */
public class SnipListRow extends Composite{
    private static Logger log = Logger.getLogger("");

    interface SnipListRowUiBinder extends UiBinder<HTMLPanel, SnipListRow> {
    }

    private static SnipListRowUiBinder ourUiBinder = GWT.create(SnipListRowUiBinder.class);

    AutoBean<SnipBean> snipBean;
    boolean viewButtons = false;

    @UiField
    FlowPanel colorStripe, snipImgParent, secondColDiv, buttonPanel;
    @UiField
    Label rep, titleLabel, snipTitle, userName, posRef, neutRef, negRef, viewCount, creationDate;
    @UiField
    Image snipImg;
    @UiField
    Button viewBtn, editBtn;

    public SnipListRow(AutoBean<SnipBean> snipBean, boolean viewButtons) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.snipBean = snipBean;
        this.viewButtons = viewButtons;
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

        titleLabel.setText(RDL.i18n.userTitle()+" "+snipBean.as().getAuthor());
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
        posRef.setText(snipBean.as().getPosRef()+" "+RDL.i18n.positiveRef());
        neutRef.setText(snipBean.as().getNeutralRef()+" "+RDL.i18n.neutralRef());
        negRef.setText(snipBean.as().getNegativeRef()+" "+RDL.i18n.negativeRef());
        viewCount.setText(snipBean.as().getViews()+" "+RDL.i18n.views());

        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.SNIP))
            snipImg.setUrl(Resources.INSTANCE.SnipImage().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.FAST_CAP))
            snipImg.setUrl(Resources.INSTANCE.FastCapImage().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.HABIT))
            snipImg.setUrl(Resources.INSTANCE.HabitImage().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.MATERIAL))
            snipImg.setUrl(Resources.INSTANCE.MaterialImage().getSafeUri().asString());


        Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSSS").parse(snipBean.as().getCreationDate());
        String dateString = DateTimeFormat.getFormat("MMM d, y").format(date);

        creationDate.setText(dateString);

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

        if(viewButtons) {
            buttonPanel.getElement().getStyle().setProperty("display","block");
            editBtn.setWidth("55px");
            viewBtn.setWidth("55px");
        }
    }

    @UiHandler("editBtn")
    public void onEditBtnClicked(ClickEvent event) {
        History.newItem(RDLConstants.Tokens.SNIP_EDIT+":"+snipBean.as().getId());
    }

    @UiHandler("viewBtn")
    public void onViewBtnClicked(ClickEvent event) {
        GuiEventBus.EVENT_BUS.fireEvent(new SnipViewEvent(snipBean.as().getId()));
    }

    public void incrementRepCounter() {
        rep.setText(snipBean.as().getRep()+1+"");
    }

    public void incrementRefCounterByRefType(String refType) {
        if(refType.equals(RDLConstants.ReferenceType.POSITIVE))
            posRef.setText(snipBean.as().getPosRef()+1+" "+RDL.i18n.positiveRef());
        else if(refType.equals(RDLConstants.ReferenceType.NEUTRAL))
            neutRef.setText(snipBean.as().getNeutralRef()+1+" "+RDL.i18n.neutralRef());
        else if(refType.equals(RDLConstants.ReferenceType.NEGATIVE))
            negRef.setText(snipBean.as().getNegativeRef()+1+" "+RDL.i18n.negativeRef());
    }

    @Override
    protected void onUnload() {
        super.onUnload();
    }
}