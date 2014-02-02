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
import com.therdl.shared.Global;
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
    AutoBean<CurrentUserBean> currentUserBean;
    boolean viewButtons = false;

    @UiField
    FlowPanel colorStripe, snipImgParent, secondColDiv;
    @UiField
    Label rep, titleLabel, userName, posRef, neutRef, negRef, viewCount, creationDate, postsCount, pledgesCount, countersCount, proposalType, proposalState;
    @UiField
    Image snipImg;
    @UiField
    Button editBtn;
    @UiField
    FlowPanel editBtnParent, postsPanel, refPanel, pledgesPanel, countersPanel, viewPanel;
    @UiField
    Anchor snipTitle;

    public SnipListRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, boolean viewButtons) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.snipBean = snipBean;
        this.currentUserBean = currentUserBean;
        this.viewButtons = viewButtons;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        // sets background color for snip img and top color strip
        if(!Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
            for(CoreCategory item : CoreCategory.values()) {
                if(item.getShortName().equals(snipBean.as().getCoreCat())) {
                    colorStripe.getElement().getStyle().setProperty("backgroundColor", item.getColCode());
                    snipImgParent.getElement().getStyle().setProperty("backgroundColor", item.getColCode());

                }
            }
        } else {
            colorStripe.getElement().getStyle().setProperty("backgroundColor", "#658cd9");
            snipImgParent.getElement().getStyle().setProperty("backgroundColor", "#658cd9");
        }


        titleLabel.setText(RDL.i18n.userTitle()+" "+snipBean.as().getAuthor());
        userName.setText(snipBean.as().getAuthor());

        // set tooltip on the snip img and top color stripe

        String toolTip = snipBean.as().getTitle()+" / "+snipBean.as().getCoreCat();

        // sets snip data
        snipImgParent.setTitle(toolTip);
        colorStripe.setTitle(toolTip);

        rep.setText(snipBean.as().getRep()+"");
        snipTitle.setText(snipBean.as().getTitle());

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS))
            snipTitle.setHref(GWT.getHostPageBaseURL()+"#"+RDLConstants.Tokens.SNIP_VIEW+":"+snipBean.as().getId());
        else if(Global.moduleName.equals(RDLConstants.Modules.STORIES))
            snipTitle.setHref(GWT.getHostPageBaseURL()+"#"+RDLConstants.Tokens.THREAD_VIEW+":"+snipBean.as().getId());
        else if(Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
            snipTitle.setHref(GWT.getHostPageBaseURL()+"#"+RDLConstants.Tokens.PROPOSAL_VIEW+":"+snipBean.as().getId());


        posRef.setText(snipBean.as().getPosRef()+" "+RDL.i18n.positiveRef());
        neutRef.setText(snipBean.as().getNeutralRef()+" "+RDL.i18n.neutralRef());
        negRef.setText(snipBean.as().getNegativeRef()+" "+RDL.i18n.negativeRef());
        viewCount.setText(snipBean.as().getViews()+" "+RDL.i18n.views());
        postsCount.setText(snipBean.as().getPosts()+" "+RDL.i18n.posts());
        pledgesCount.setText(snipBean.as().getPledges()+" "+RDL.i18n.pledges());
        countersCount.setText(snipBean.as().getCounters()+" "+RDL.i18n.counters());

        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.SNIP))
            snipImg.setUrl(Resources.INSTANCE.SnipImage().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.FAST_CAP))
            snipImg.setUrl(Resources.INSTANCE.FastCapImage().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.HABIT))
            snipImg.setUrl(Resources.INSTANCE.HabitImage().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.MATERIAL))
            snipImg.setUrl(Resources.INSTANCE.MaterialImage().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.THREAD))
            snipImg.setUrl(Resources.INSTANCE.ThreadImageGif().getSafeUri().asString());
        if(snipBean.as().getSnipType().equals(RDLConstants.SnipType.PROPOSAL))
            snipImg.setUrl(Resources.INSTANCE.ProposalImageGif().getSafeUri().asString());


        Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSSS").parse(snipBean.as().getCreationDate());
        String dateString = DateTimeFormat.getFormat("MMM d, y").format(date);

        creationDate.setText(dateString);

        // create badge table

        Grid badgeGrid = new Grid(2,4);
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 4; ++col) {
                Image img = new Image(Resources.INSTANCE.badgeImage().getSafeUri().asString());
                img.setWidth("27px");
                img.setHeight("30px");
                badgeGrid.setWidget(row, col, img);
            }
        }

        if(secondColDiv.getWidgetCount() == 0)
            secondColDiv.add(badgeGrid);

        if(viewButtons) {
            if(currentUserBean.as().isAuth() && currentUserBean.as().getName().equals(snipBean.as().getAuthor())) {
                editBtnParent.getElement().getStyle().setProperty("display","block");
            }
            editBtn.setWidth("55px");
        }

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
            postsPanel.getElement().getStyle().setProperty("display","none");
            pledgesPanel.getElement().getStyle().setProperty("display","none");
            countersPanel.getElement().getStyle().setProperty("display","none");
        } else if(Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
            refPanel.getElement().getStyle().setProperty("display","none");
            pledgesPanel.getElement().getStyle().setProperty("display","none");
            countersPanel.getElement().getStyle().setProperty("display","none");
        } else if(Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
            postsPanel.getElement().getStyle().setProperty("display","none");
            refPanel.getElement().getStyle().setProperty("display","none");
            viewPanel.getElement().getStyle().setProperty("display","none");

            proposalType.setText(RDL.i18n.type()+": "+snipBean.as().getProposalType());
            proposalState.setText(RDL.i18n.state()+": "+snipBean.as().getProposalState());
        }
    }

    @UiHandler("editBtn")
    public void onEditBtnClicked(ClickEvent event) {
        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS))
            History.newItem(RDLConstants.Tokens.SNIP_EDIT+":"+snipBean.as().getId());
        else if(Global.moduleName.equals(RDLConstants.Modules.STORIES))
            History.newItem(RDLConstants.Tokens.THREAD_EDIT+":"+snipBean.as().getId());
        else if(Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
            History.newItem(RDLConstants.Tokens.PROPOSAL_EDIT+":"+snipBean.as().getId());
    }

//    @UiHandler("snipTitle")
//    public void onViewBtnClicked(ClickEvent event) {
//        GuiEventBus.EVENT_BUS.fireEvent(new SnipViewEvent(snipBean.as().getId()));
//    }

    public void incrementRepCounter() {
        rep.setText(snipBean.as().getRep()+1+"");
    }

    public void incrementRefCounterByRefType(String refType, String snipType) {
        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
            if(refType.equals(RDLConstants.ReferenceType.POSITIVE))
                posRef.setText(snipBean.as().getPosRef()+1+" "+RDL.i18n.positiveRef());
            else if(refType.equals(RDLConstants.ReferenceType.NEUTRAL))
                neutRef.setText(snipBean.as().getNeutralRef()+1+" "+RDL.i18n.neutralRef());
            else if(refType.equals(RDLConstants.ReferenceType.NEGATIVE))
                negRef.setText(snipBean.as().getNegativeRef()+1+" "+RDL.i18n.negativeRef());
        } else if(Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
            postsCount.setText(snipBean.as().getPosts()+1+" "+RDL.i18n.posts());
        } else if(Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
            if(snipType.equals(RDLConstants.SnipType.PLEDGE))
                pledgesCount.setText(snipBean.as().getPledges()+1+" "+RDL.i18n.pledges());
            else if(snipType.equals(RDLConstants.SnipType.COUNTER))
                countersCount.setText(snipBean.as().getCounters()+1+" "+RDL.i18n.counters());
        }
    }

    @Override
    protected void onUnload() {
        super.onUnload();
    }
}