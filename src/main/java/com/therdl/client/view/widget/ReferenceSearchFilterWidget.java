package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.impl.SnipViewImpl;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

public class ReferenceSearchFilterWidget extends Composite{
    interface ReferenceSearchFilterWidgetUiBinder extends UiBinder<HTMLPanel, ReferenceSearchFilterWidget> {
    }

    private static ReferenceSearchFilterWidgetUiBinder ourUiBinder = GWT.create(ReferenceSearchFilterWidgetUiBinder.class);

    @UiField
    FlowPanel refTypePanel, authorRepPanel, authorNamePanel, datePanel;

    @UiField
    TextBox authorRep, authorName;

    @UiField
    DateFilterWidget dateFilterWidget;

    @UiField
    Label typeLabel, filterLabel;

    Image selectedArrow;

    // default sort order is descending by creation date
    private int sortOrder = -1;
    private String sortField = RDLConstants.SnipFields.CREATION_DATE;

    private CheckBox[] checkBoxArray = new CheckBox[3];
    private String[] referenceTypes = new String[3];

    private Beanery beanery = GWT.create(Beanery.class);
    private SnipViewImpl view;

    public ReferenceSearchFilterWidget(SnipViewImpl view) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.view = view;

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
            initRefTypeCheckboxes();
        } else {
            typeLabel.getElement().getStyle().setProperty("display","none");
            filterLabel.setText(RDL.i18n.filterPosts());
        }
        createSortArrows();
    }

    private void initRefTypeCheckboxes() {
        referenceTypes = new String[] {
                RDLConstants.ReferenceType.POSITIVE,
                RDLConstants.ReferenceType.NEUTRAL,
                RDLConstants.ReferenceType.NEGATIVE,

        };

        String[] referenceTypesText = new String[] {
                RDL.i18n.positiveShort(),
                RDL.i18n.neutral(),
                RDL.i18n.negativeShort(),

        };

        for (int i=0; i<referenceTypes.length; i++) {
            checkBoxArray[i] = new CheckBox(referenceTypesText[i]);
            checkBoxArray[i].setStyleName("checkBoxBtn");
            checkBoxArray[i].setValue(true);
            if(i==0) {
                checkBoxArray[i].getElement().getStyle().setProperty("marginLeft","0px");
            }

            refTypePanel.add(checkBoxArray[i]);

        }
    }

    private String getCheckedFlags() {
        String checkedFlags = "";
        for (int j=0; j<checkBoxArray.length; j++) {
            if(checkBoxArray[j].getValue()) {
                checkedFlags += referenceTypes[j]+",";
            }
        }
        if(!checkedFlags.equals(""))
            checkedFlags = checkedFlags.substring(0,checkedFlags.length()-1);

        return checkedFlags;
    }

    /**
     * sets sort arrows for some search fields (views, rep, pos/neut/neg ref, author, date), down for descending order, up for ascending order
     * default order is descending order by creation date
     */
    private void createSortArrows() {
        FlowPanel[] flowPanels = {authorRepPanel, authorNamePanel, datePanel};
        String[] keyNames = {RDLConstants.SnipFields.REP, RDLConstants.SnipFields.AUTHOR, RDLConstants.SnipFields.CREATION_DATE};

        for (int i=0; i<flowPanels.length; i++) {
            final String keyName = keyNames[i];
            FlowPanel arrowPanel = new FlowPanel();
            Image imgUp = new Image(Resources.INSTANCE.arrowUpGrey().getSafeUri().asString());
            imgUp.setWidth("15px");
            imgUp.setStyleName("arrowImg");
            imgUp.setTitle(RDL.i18n.sortAsc());
            arrowPanel.add(imgUp);

            imgUp.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    if(sortOrder == 1)
                        selectedArrow.setUrl(Resources.INSTANCE.arrowUpGrey().getSafeUri().asString());
                    else
                        selectedArrow.setUrl(Resources.INSTANCE.arrowDownGrey().getSafeUri().asString());

                    selectedArrow = (Image) clickEvent.getSource();
                    sortOrder = 1;
                    sortField = keyName;
                    selectedArrow.setUrl(Resources.INSTANCE.arrowUpGreen().getSafeUri().asString());

                    view.getSnipReferences(formSearchOptionsBean());
                }
            });

            Image imgDown = new Image(Resources.INSTANCE.arrowDownGrey().getSafeUri().asString());
            imgDown.setWidth("15px");
            imgDown.setTitle(RDL.i18n.sortDesc());
            imgDown.setStyleName("arrowImg");
            arrowPanel.add(imgDown);

            imgDown.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    if(sortOrder == 1)
                        selectedArrow.setUrl(Resources.INSTANCE.arrowUpGrey().getSafeUri().asString());
                    else
                        selectedArrow.setUrl(Resources.INSTANCE.arrowDownGrey().getSafeUri().asString());

                    selectedArrow = (Image) clickEvent.getSource();
                    sortOrder = -1;
                    sortField = keyName;
                    selectedArrow.setUrl(Resources.INSTANCE.arrowDownGreen().getSafeUri().asString());

                    view.getSnipReferences(formSearchOptionsBean());
                }
            });

            arrowPanel.getElement().getStyle().setProperty("float","right");
            arrowPanel.getElement().getStyle().setProperty("marginTop","2px");
            flowPanels[i].add(arrowPanel);

            if(flowPanels[i].equals(datePanel)) {
                selectedArrow = imgDown;
                selectedArrow.setUrl(Resources.INSTANCE.arrowDownGreen().getSafeUri().asString());

            }
        }


    }


    @UiHandler("refFilter")
    public void filterReferences(ClickEvent event) {
        view.getSnipReferences(formSearchOptionsBean());
    }

    private AutoBean<SnipBean> formSearchOptionsBean() {
        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();

        if(!authorRep.getText().equals(""))
            searchOptionsBean.as().setRep(Integer.parseInt(authorRep.getText()));

        if(!authorName.getText().equals(""))
            searchOptionsBean.as().setAuthor(authorName.getText());

        if(!dateFilterWidget.getDateFrom().equals(""))
            searchOptionsBean.as().setDateFrom(dateFilterWidget.getDateFrom());

        if(!dateFilterWidget.getDateTo().equals(""))
            searchOptionsBean.as().setDateFrom(dateFilterWidget.getDateTo());

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS))
            searchOptionsBean.as().setReferenceType(getCheckedFlags());
        searchOptionsBean.as().setSortOrder(sortOrder);
        searchOptionsBean.as().setSortField(sortField);

        return searchOptionsBean;
    }
}