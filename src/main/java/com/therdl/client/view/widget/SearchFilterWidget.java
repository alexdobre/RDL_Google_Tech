package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SubCategory;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;

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
    com.github.gwtbootstrap.client.ui.Button createNewButton;

    @UiField
    TextBox title, content, author, posRef, neutralRef, negativeRef, postCount, viewCount, snipRep, pledgesCount, countersCount;

    @UiField
    HTMLPanel container;

    @UiField
    ListBox categoryList, proposalTypeList, proposalStateList;

 //   @UiField
 //   ListBox subCategoryList;

    @UiField
    DateFilterWidget dateFilterWidget;

    @UiField
    FlowPanel refPanel;

    @UiField
    Label typeLabel, refLabel, postLabel, filterLabel, categoryLabel, viewsLabel, contentLabel, proposalTypeLabel, proposalStateLabel, pledgesCountLabel, countersCountLabel;


    @UiField
    FlowPanel viewPanel, repPanel, authorPanel, datePanel, posRefPanel, neutralRefPanel, negativeRefPanel, postPanel, typePanel, pledgesPanel, countersPanel;

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

    SearchView view;

    private BookmarkSearchPopup bookmarkSearchPopup;
    private String editPageToken;

    private Beanery beanery = GWT.create(Beanery.class);

    interface SnipSearchWidgetUiBinder extends UiBinder<Widget, SearchFilterWidget> { }

    public SearchFilterWidget(SearchView snipSearchView) {
        initWidget(uiBinder.createAndBindUi(this));
        this.view = snipSearchView;

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
            editPageToken = RDLConstants.Tokens.SNIP_EDIT;

            createTypeFilter();

            posRef.getElement().getStyle().setProperty("border","1px solid green");
            neutralRef.getElement().getStyle().setProperty("border","1px solid grey");
            negativeRef.getElement().getStyle().setProperty("border","1px solid red");

            postLabel.getElement().getStyle().setProperty("display","none");
            postPanel.getElement().getStyle().setProperty("display","none");

            hideProposalItems();

            filterLabel.setText(RDL.i18n.filterSnips());

            createCategoryList();


        } else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
            editPageToken = RDLConstants.Tokens.THREAD_EDIT;

            typeLabel.getElement().getStyle().setProperty("display","none");
            refPanel.getElement().getStyle().setProperty("display","none");
            refLabel.getElement().getStyle().setProperty("display","none");
            hideProposalItems();

            filterLabel.setText(RDL.i18n.filterThreads());

            createCategoryList();
        } else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
            editPageToken = RDLConstants.Tokens.PROPOSAL_EDIT;

            typeLabel.getElement().getStyle().setProperty("display","none");
            refPanel.getElement().getStyle().setProperty("display","none");
            refLabel.getElement().getStyle().setProperty("display","none");
            postLabel.getElement().getStyle().setProperty("display","none");
            postPanel.getElement().getStyle().setProperty("display","none");

            categoryList.getElement().getStyle().setProperty("display","none");
            categoryLabel.getElement().getStyle().setProperty("display","none");

            viewsLabel.getElement().getStyle().setProperty("display","none");
            viewPanel.getElement().getStyle().setProperty("display","none");

            contentLabel.getElement().getStyle().setProperty("display","none");
            content.getElement().getStyle().setProperty("display","none");

            ViewUtils.createProposalTypeList(proposalTypeList);
            ViewUtils.createProposalStateList(proposalStateList);

        }

        createSortArrows();

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
        FlowPanel[] flowPanels = {viewPanel, repPanel, authorPanel, datePanel, posRefPanel, neutralRefPanel, negativeRefPanel, postPanel, pledgesPanel, countersPanel};
        String[] keyNames = {RDLConstants.SnipFields.VIEWS, RDLConstants.SnipFields.REP,
                            RDLConstants.SnipFields.AUTHOR, RDLConstants.SnipFields.CREATION_DATE,
                            RDLConstants.SnipFields.POS_REF, RDLConstants.SnipFields.NEUTRAL_REF,
                            RDLConstants.SnipFields.NEGATIVE_REF, RDLConstants.SnipFields.POSTS,
                            RDLConstants.SnipFields.PLEDGES, RDLConstants.SnipFields.COUNTERS
                        };

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
     * creates category list for snips
     */
    void createCategoryList() {
     //   categoryList.addItem("Select a category");
        int i=0;
        for(CoreCategory item : CoreCategory.values()) {
            categoryList.addItem(item.getShortName());
            categoryList.setValue(i, item.getShortName());
            i++;
        }
    }

    private void hideProposalItems() {
        proposalStateLabel.getElement().getStyle().setProperty("display","none");
        proposalStateList.getElement().getStyle().setProperty("display","none");

        proposalTypeLabel.getElement().getStyle().setProperty("display","none");
        proposalTypeList.getElement().getStyle().setProperty("display","none");

        pledgesCountLabel.getElement().getStyle().setProperty("display","none");
        pledgesPanel.getElement().getStyle().setProperty("display","none");

        countersCountLabel.getElement().getStyle().setProperty("display","none");
        countersPanel.getElement().getStyle().setProperty("display","none");
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

        if(!Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
            String categories = ViewUtils.getSelectedItems(categoryList);
            if(!categories.equals("")) {
                searchOptionsBean.as().setCoreCat(categories);
            }
        } else {
            String proposalTypes = ViewUtils.getSelectedItems(proposalTypeList);
            if(!proposalTypes.equals("")) {
                searchOptionsBean.as().setProposalType(proposalTypes);
            }

            String proposalStates = ViewUtils.getSelectedItems(proposalStateList);
            if(!proposalStates.equals("")) {
                searchOptionsBean.as().setProposalState(proposalStates);
            }
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

        String postsText = postCount.getText();
        if(!postsText.equals("")) {
            searchOptionsBean.as().setPosts(Integer.parseInt(postsText));
        }

        String pledgesText = pledgesCount.getText();
        if(!pledgesText.equals("")) {
            searchOptionsBean.as().setPledges(Integer.parseInt(pledgesText));
        }

        String countersText =  countersCount.getText();
        if(!countersText.equals("")) {
            searchOptionsBean.as().setCounters(Integer.parseInt(countersText));
        }

        String snipRepText = snipRep.getText();
        if(!snipRepText.equals("")) {
            searchOptionsBean.as().setRep(Integer.parseInt(snipRepText));
        }

        searchOptionsBean.as().setSortOrder(sortOrder);
        searchOptionsBean.as().setSortField(sortField);

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS))
            searchOptionsBean.as().setSnipType(getCheckedSnipTypes());
        else if(Global.moduleName.equals(RDLConstants.Modules.STORIES))
            searchOptionsBean.as().setSnipType(RDLConstants.SnipType.THREAD);
        else if(Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
            searchOptionsBean.as().setSnipType(RDLConstants.SnipType.PROPOSAL);

        return searchOptionsBean;
    }

    /**
     * checks which snip type checkboxes are checked
     * @return returns snip type values separated by comma
     */
    public String getCheckedSnipTypes() {
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
        if(view.getPresenter().getController().getCurrentUserBean().as().isAuth()) {
            if(Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS) && !view.getCurrentUserBean().as().getIsRDLSupporter())
                Window.alert(RDL.i18n.proposalCreateMsg());
            else
                History.newItem(editPageToken);
        } else {
            String page = editPageToken;
            if(Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS) && !view.getCurrentUserBean().as().getIsRDLSupporter())
                page = RDLConstants.Tokens.IMPROVEMENTS;

            log.info("page="+page);
            view.getPresenter().getController().getWelcomeView().showLoginPopUp(createNewButton.getAbsoluteLeft()+90, createNewButton.getAbsoluteTop()-120, page);
        }
	}

    /**
     * sets filter's fields by given bean
     * @param searchOptionsBean
     */

    public void setSearchFilterFields(AutoBean<SnipBean> searchOptionsBean) {
        title.setText(searchOptionsBean.as().getTitle() != null ? searchOptionsBean.as().getTitle() : "");
        content.setText(searchOptionsBean.as().getContent() != null ? searchOptionsBean.as().getContent() : "");
        author.setText(searchOptionsBean.as().getAuthor() != null ? searchOptionsBean.as().getAuthor() : "");
        posRef.setText(searchOptionsBean.as().getPosRef() != null ? searchOptionsBean.as().getPosRef()+"" : "");
        neutralRef.setText(searchOptionsBean.as().getNeutralRef() != null ? searchOptionsBean.as().getNeutralRef()+"" : "");
        negativeRef.setText(searchOptionsBean.as().getNegativeRef() != null ? searchOptionsBean.as().getNegativeRef()+"" : "");
        postCount.setText(searchOptionsBean.as().getPosts() != null ? searchOptionsBean.as().getPosts()+"" : "");
        viewCount.setText(searchOptionsBean.as().getViews() != null ? searchOptionsBean.as().getViews()+"" : "");
        pledgesCount.setText(searchOptionsBean.as().getPledges() != null ? searchOptionsBean.as().getPledges()+"" : "");
        countersCount.setText(searchOptionsBean.as().getCounters() != null ? searchOptionsBean.as().getCounters()+"" : "");
        snipRep.setText(searchOptionsBean.as().getRep() != null ? searchOptionsBean.as().getRep()+"" : "");

        dateFilterWidget.setDateFrom(searchOptionsBean.as().getDateFrom() != null ? searchOptionsBean.as().getDateFrom()+"" : "");
        dateFilterWidget.setDateTo(searchOptionsBean.as().getDateTo() != null ? searchOptionsBean.as().getDateTo()+"" : "");


        if(searchOptionsBean.as().getCoreCat() != null) {
            for(int i=0; i<categoryList.getItemCount(); i++) {
                if(categoryList.getItemText(i).equals(searchOptionsBean.as().getCoreCat())) {
                    categoryList.setSelectedIndex(i);
                    break;
                }
            }
        } //else {
//            categoryList.setSelectedIndex(0);
//        }

        if(Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
            if(searchOptionsBean.as().getSnipType() != null && searchOptionsBean.as().getSnipType() != "") {
                log.info("snipTypes="+searchOptionsBean.as().getSnipType());
                String[] snipTypes = searchOptionsBean.as().getSnipType().split(",");
                for (int j=0; j<checkBoxArray.length; j++) {
                    checkBoxArray[j].setValue(false);
                    for(int i=0; i<snipTypes.length; i++) {
                        if(checkBoxArray[j].getText().equals(snipTypeHm.get(snipTypes[i])))
                            checkBoxArray[j].setValue(true);

                    }
                }
            } else {
                for (int j=0; j<checkBoxArray.length; j++) {
                    checkBoxArray[j].setValue(true);
                }
            }
        }
    }

}
