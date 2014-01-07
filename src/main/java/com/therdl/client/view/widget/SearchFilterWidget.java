package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SubCategory;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

import java.util.*;
import java.util.logging.Logger;

/**
 * GWT widget class for search filter
 * creates GUI elements and handlers for them
 */

public class SearchFilterWidget extends Composite {

    private static Logger log = Logger.getLogger("");

    private static SnipSearchWidgetUiBinder uiBinder = GWT.create(SnipSearchWidgetUiBinder.class);

    @UiField
    com.github.gwtbootstrap.client.ui.Button submit;

    @UiField
    com.github.gwtbootstrap.client.ui.Button getLinkBtn;

    @UiField
    TextBox title;

    @UiField
    TextBox content;

    @UiField
    TextBox author;

    @UiField
    TextBox posRef;

    @UiField
    TextBox neutralRef;

    @UiField
    TextBox negativeRef;

    @UiField
    TextBox viewCount;

    @UiField
    TextBox snipRep;

    @UiField
    HTMLPanel container;

    @UiField
    ListBox categoryList;

    @UiField
    ListBox subCategoryList;

    @UiField
    DateFilterWidget dateFilterWidget;


    @UiField
    FlowPanel viewPanel, repPanel, authorPanel, datePanel, posRefPanel, neutralRefPanel, negativeRefPanel, typePanel;

   // DatePicker datePicker;

    Image selectedArrow;

    // default sort order is descending by creation date
    private int sortOrder = -1;
    private String sortField = RDLConstants.SnipFields.CREATION_DATE;

    private CheckBox[] checkBoxArray = new CheckBox[4];
    private LinkedHashMap<String, String> snipTypeHm = new LinkedHashMap();

    public String getSortField() {
        return sortField;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    SnipSearchView view;

    private BookmarkSearchPopup bookmarkSearchPopup;
    private String authorName;

    private Beanery beanery = GWT.create(Beanery.class);

    interface SnipSearchWidgetUiBinder extends UiBinder<Widget, SearchFilterWidget> { }

    public SearchFilterWidget(SnipSearchViewImpl snipSearchView, String authorName) {
        initWidget(uiBinder.createAndBindUi(this));
        this.view = snipSearchView;
        this.authorName = authorName;

        createTypeFilter();
        createCategoryList();
        createSortArrows();

        posRef.getElement().getStyle().setProperty("border","1px solid green");
        neutralRef.getElement().getStyle().setProperty("border","1px solid grey");
        negativeRef.getElement().getStyle().setProperty("border","1px solid red");

        if(this.authorName != null)
            author.setText(authorName);
	}

    /**
     * create checkbox group for snip type (snip, fastcap, material, habit)
     */
    private void createTypeFilter() {
        snipTypeHm.put(RDLConstants.SnipType.SNIP, RDL.i18n.snip());
        snipTypeHm.put(RDLConstants.SnipType.FAST_CAP, RDL.i18n.fastCap());
        snipTypeHm.put(RDLConstants.SnipType.MATERIAL, RDL.i18n.material());
        snipTypeHm.put(RDLConstants.SnipType.HABIT, RDL.i18n.habit());

        Iterator itHm = snipTypeHm.entrySet().iterator();
        int i=0;
        while(itHm.hasNext()) {
            Map.Entry pairs = (Map.Entry) itHm.next();

            checkBoxArray[i] = new CheckBox((String) pairs.getValue());
            checkBoxArray[i].setValue(true);
            checkBoxArray[i].setStyleName("checkBoxBtn");
            if(pairs.getKey().equals(RDLConstants.SnipType.FAST_CAP)) {
                checkBoxArray[i].getElement().getStyle().setProperty("marginLeft","36px");
            }

            typePanel.add(checkBoxArray[i]);
            i++;
        }

    }

    /**
     * sets sort arrows for some search fields (views, rep, pos/neut/neg ref, author, date), down for descending order, up for ascending order
     * default order is descending order by creation date
     */
    private void createSortArrows() {
        FlowPanel[] flowPanels = {viewPanel, repPanel, authorPanel, datePanel, posRefPanel, neutralRefPanel, negativeRefPanel};
        String[] keyNames = {RDLConstants.SnipFields.VIEWS, RDLConstants.SnipFields.REP, RDLConstants.SnipFields.AUTHOR, RDLConstants.SnipFields.CREATION_DATE, RDLConstants.SnipFields.POS_REF, RDLConstants.SnipFields.NEUTRAL_REF, RDLConstants.SnipFields.NEGATIVE_REF};

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

                    view.doFilterSearch(formSearchOptionBean(), 0);
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

                    view.doFilterSearch(formSearchOptionBean(), 0);
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

    /**
     * creates category and subcategory list for snips, when user choose a category subcategory list is refreshed
     */
    void createCategoryList() {
        categoryList.addItem("Select a category");
        for(CoreCategory item : CoreCategory.values()) {
            categoryList.addItem(item.getShortName());
        }

        categoryList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                int selectedIndex = categoryList.getSelectedIndex();
                subCategoryList.clear();
                subCategoryList.addItem("Select a subcategory");
                subCategoryList.setEnabled(false);

                if (selectedIndex != 0) {
                    EnumSet subCategories = CoreCategory.values()[selectedIndex - 1].getSubCategories();

                    if (subCategories != null) {
                        for (Iterator it = subCategories.iterator(); it.hasNext(); ) {
                            subCategoryList.addItem(((SubCategory) it.next()).getName());
                        }
                        subCategoryList.setEnabled(true);
                    }
                }
            }
        });

        subCategoryList.addItem("Select a subcategory");
        subCategoryList.setEnabled(false);
    }

    /**
     * click handle for link button for bookmark search
     * bookmark search popup is opened in the button side
     * @param event
     */

    @UiHandler("getLinkBtn")
    public void onLinkBtnClicked(ClickEvent event) {
        BookmarkSearchPopup bookmarkSearchPopup = new BookmarkSearchPopup(this);
        int x = getLinkBtn.getAbsoluteLeft();
        int y = getLinkBtn.getAbsoluteTop();
        bookmarkSearchPopup.setPopupPosition(x+getLinkBtn.getOffsetWidth(), y);
        bookmarkSearchPopup.show();
    }

    /**
     * construct snip bean object from the popup fields
     * at the end when there is no any search option gets initial list, else do the filtered search
     * @param event
     */

    @UiHandler("submit")
    public void onSubmit(ClickEvent event) {
        view.doFilterSearch(formSearchOptionBean(), 0);
    }

    /**
     * forms search option bean from filter form elements
     * @return search option bean as SnipBean object
     */
    private AutoBean<SnipBean> formSearchOptionBean() {
        AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
        String titleText = title.getText();
        if(!titleText.equals("")) {
            searchOptionsBean.as().setTitle(titleText);
        }

        String contentText = content.getText();
        if(!contentText.equals("")) {
            searchOptionsBean.as().setContent(contentText);
        }

        String authorText = author.getText();
        if(!authorText.equals("")) {
            searchOptionsBean.as().setAuthor(authorText);
        }

        String dateFromText = dateFilterWidget.getDateFrom();
        if(!dateFromText.equals("")) {
            searchOptionsBean.as().setDateFrom(dateFromText);
        }

        String dateToText = dateFilterWidget.getDateTo();
        if(!dateToText.equals("")) {
            searchOptionsBean.as().setDateTo(dateToText);
        }

        int catIndex = categoryList.getSelectedIndex();
        if(catIndex != 0) {
            searchOptionsBean.as().setCoreCat(categoryList.getItemText(catIndex));
        }

        int subCatIndex = subCategoryList.getSelectedIndex();
        if(subCatIndex != 0) {
            searchOptionsBean.as().setSubCat(subCategoryList.getItemText(subCatIndex));
        }

        String posRefText = posRef.getText();
        if(!posRefText.equals("")) {
            searchOptionsBean.as().setPosRef(Integer.parseInt(posRefText));
        }

        String neutralRefText = neutralRef.getText();
        if(!neutralRefText.equals("")) {
            searchOptionsBean.as().setNeutralRef(Integer.parseInt(neutralRefText));
        }

        String negativeRefText = negativeRef.getText();
        if(!negativeRefText.equals("")) {
            searchOptionsBean.as().setNegativeRef(Integer.parseInt(negativeRefText));
        }

        String viewsText = viewCount.getText();
        if(!viewsText.equals("")) {
            searchOptionsBean.as().setViews(Integer.parseInt(viewsText));
        }

        String snipRepText = snipRep.getText();
        if(!snipRepText.equals("")) {
            searchOptionsBean.as().setRep(Integer.parseInt(snipRepText));
        }

        searchOptionsBean.as().setSortOrder(sortOrder);
        searchOptionsBean.as().setSortField(sortField);
        searchOptionsBean.as().setSnipType(getCheckedSnipTypes());
        return searchOptionsBean;
    }

    /**
     * checks which snip type checkboxes are checked
     * @return returns snip type values separated by comma
     */
    private String getCheckedSnipTypes() {
        List keys = new ArrayList(snipTypeHm.keySet());
        String checkedFlags = "";
        for (int j=0; j<checkBoxArray.length; j++) {
            if(checkBoxArray[j].getValue()) {
                checkedFlags += keys.get(j)+",";
            }
        }
        if(!checkedFlags.equals(""))
            checkedFlags = checkedFlags.substring(0,checkedFlags.length()-1);

        return checkedFlags;
    }

    /**
     * handler for the create new button
     * opens create/edit snip view
     * @param event
     */
	@UiHandler("createNewButton")
	void onCreateNewButtonClick(ClickEvent event) {
        History.newItem(RDLConstants.Tokens.SNIP_EDIT);
	}

}
