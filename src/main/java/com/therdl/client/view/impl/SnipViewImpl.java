package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.therdl.client.RDL;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.widget.*;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.widget.editor.RichTextToolbar;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * SnipViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI so user can view a Snip edit, is brought into focus
 * fired from a Closure  new Religion
 *
 * @ SnipViewPresenter.Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ Map<String, String> currentSnipView, this is a store for the parameters
 * to be extracted from the client side snip data, these parameters should be
 * used to call the backend to retrieve the full snip for this view, under construction
 * @ AppMenu appMenu the upper menu view
 */
public class SnipViewImpl extends Composite implements SnipView {

    private static Logger log = Logger.getLogger("");

    private final AutoBean<CurrentUserBean> currentUserBean;

    interface SnipViewImpllUiBinder extends UiBinder<Widget, SnipViewImpl> {
    }

    private static SnipViewImpllUiBinder uiBinder = GWT.create(SnipViewImpllUiBinder.class);

    private SnipView.Presenter presenter;
    private Beanery beanery = GWT.create(Beanery.class);

    Map<String, String> currentSnipView;
    private AutoBean<SnipBean> currentSnipBean;

    // uibinder variables

    @UiField
    AppMenu appMenu;

    @UiField
    FlowPanel snipViewCont, referenceCont, radioBtnParent, referenceListCont, bottomCont, checkboxBtnParent;
    @UiField
    RichTextArea richTextArea;
    @UiField
    EditorWidget editorWidget;
    @UiField
    Button showRef, leaveRef, saveRef, closeRef;
    @UiField
    RadioButton rb1, rb2, rb3;
    @UiField
    Image repBtn;
    @UiField
    HTMLPanel rootPanel;
    @UiField
    LoadingWidget loadingWidget;

    SnipListRow snipListRow;

    public SnipViewImpl(AutoBean<CurrentUserBean> currentUserBean) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUserBean = currentUserBean;
        setAppMenu(currentUserBean);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;

    }

    /**
     * Sets the upper header Menu to the correct state for supplied credentials
     * called from presenter
     *
     * @param currentUserBean AutoBean<CurrentUserBean> currentUserBean)
     */
    @Override
    public void setAppMenu(AutoBean<CurrentUserBean> currentUserBean) {

        if (currentUserBean.as().isAuth()) {
            log.info("ProfileViewImpl setAppMenu auth true " + currentUserBean.as().getName());

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(currentUserBean.as().getName());
            this.appMenu.setEmail(currentUserBean.as().getEmail());
            this.appMenu.setLogInVisible(false);
        } else {

            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }
    }

    @Override
    protected void onUnload() {
        referenceListCont.clear();
        snipViewCont.clear();
        checkboxBtnParent.clear();
    }

    /**
     * creates main gwt widgets of snip view page
     * @param snipBean snip as SnipBean object
     */
    public void viewSnip(AutoBean<SnipBean> snipBean) {
        this.currentSnipBean = snipBean;

        // this is the top widget, like in the list widget
        snipListRow = new SnipListRow(snipBean, currentUserBean, false);
        snipViewCont.add(snipListRow);
        richTextArea.setHTML(snipBean.as().getContent());
        richTextArea.setEnabled(false);
        leaveRef.getElement().getStyle().setProperty("marginLeft", "10px");
        showRef.setText(RDL.i18n.showReferences());
        referenceCont.getElement().getStyle().setProperty("display", "none");

        // hide give reputation/leave reference buttons when user already gave a reputation/wrote a reference
        repBtn.getElement().getStyle().setProperty("display", snipBean.as().getAuthor().equals(currentUserBean.as().getName()) || snipBean.as().getIsRepGivenByUser() == 1 ? "none" : "");
        leaveRef.getElement().getStyle().setProperty("display", snipBean.as().getAuthor().equals(currentUserBean.as().getName()) || snipBean.as().getIsRefGivenByUser() == 1 ? "none" : "");
    }

    /**
     * handler of the leave reference button. Opens an editor for creating new reference
     * @param event ClickEvent
     */
    @UiHandler("leaveRef")
    public void onLeaveRefClicked(ClickEvent event) {
        referenceCont.getElement().getStyle().setProperty("display", "block");
        closeRef.getElement().getStyle().setProperty("marginLeft", "10px");
        referenceListCont.getElement().getStyle().setProperty("display", "none");
        checkboxBtnParent.clear();
        editorWidget.setHTML("");
        showRef.setText(RDL.i18n.showReferences());
    }

    /**
     * handler of the closeRef button
     * closes editor
     * @param event
     */
    @UiHandler("closeRef")
    public void onCloseRefClicked(ClickEvent event) {
        referenceCont.getElement().getStyle().setProperty("display", "none");
    }

    /**
     * handler of the showRef button.
     * Calls presenter's function to retrieve references for the current viewed snip
     * @param event
     */
    @UiHandler("showRef")
    public void onShowRefClicked(ClickEvent event) {
        if(showRef.getText().equals(RDL.i18n.showReferences())) {
            loadingWidget.getElement().getStyle().setProperty("display","block");
            presenter.getSnipReferences("positive,negative,neutral", 0);
        } else {
            bottomCont.getElement().getStyle().setProperty("display", "none");
            showRef.setText(RDL.i18n.showReferences());
        }
    }

    @UiHandler("repBtn")
    public void onRepBtnClicked(ClickEvent event) {
        presenter.giveSnipReputation();
    }

    /**
     * when user clicks give reputation button, request is sent to increment reputation counter for the snip
     * in response handler call this function to hide button and also increment rep counter in the view
     */
    public void giveRepResponseHandler() {
        repBtn.getElement().getStyle().setProperty("display", "none");
        snipListRow.incrementRepCounter();
    }

    /**
     * when user clicks save reference button, request is sent to save reference and also the counter by refType is incremented
     * in response handler call this function to hide leave reference button and
     * also increment ref counter in the view for the saved reference type
     * @param refType saved reference type
     */
    public void saveReferenceResponseHandler(String refType) {
        leaveRef.getElement().getStyle().setProperty("display", "none");
        snipListRow.incrementRefCounterByRefType(refType);
    }

    /**
     * saves a reference
     * constructs bean object for reference and calls presenter's function to save created reference
     * @param event
     */
    @UiHandler("saveRef")
    public void onSaveRefClicked(ClickEvent event) {
        if(editorWidget.getHTML().equals("")) {
            Window.alert("Reference content cannot be empty.");
            return;
        }

        String referenceType = RDLConstants.ReferenceType.POSITIVE;

        if(rb2.getValue()) {
            referenceType = RDLConstants.ReferenceType.NEUTRAL;
        } else if(rb3.getValue()) {
            referenceType = RDLConstants.ReferenceType.NEGATIVE;
        }

        // set data for reference object
        AutoBean<SnipBean> newBean = beanery.snipBean();
        newBean.as().setContent(editorWidget.getHTML());
        newBean.as().setReferenceType(referenceType);
        newBean.as().setSnipType(RDLConstants.SnipType.REFERENCE);
        newBean.as().setAuthor(currentUserBean.as().getName());
        newBean.as().setRep(0);

        // this is parent snip id
        newBean.as().setId(currentSnipBean.as().getId());

        presenter.saveReference(newBean);
    }

    /**
     * shows references in a tab panel with paging
     * @param beanList list of references as bean objects
     */
    public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, String pReferenceTypes, int pageIndex) {
        loadingWidget.getElement().getStyle().setProperty("display","none");
        showRef.setText(RDL.i18n.hideReferences());
        referenceListCont.clear();
        checkboxBtnParent.clear();
        bottomCont.getElement().getStyle().setProperty("display", "block");
        referenceListCont.getElement().getStyle().setProperty("display", "block");
        referenceCont.getElement().getStyle().setProperty("display", "none");

        // default size of rows in one tab
        int listRowSize = Constants.DEFAULT_REFERENCE_PAGE_SIZE;
        int tabCount = 1;
        if(beanList.size() != 0) {
            tabCount = (int) Math.ceil((double)beanList.get(0).as().getCount()/listRowSize);
        }

        TabPanel tabPanel = new TabPanel();

        // creates tabs of count tabCount
        for (int i=1; i<=tabCount; i++) {
            // creates content of current tab
            FlowPanel tabContent = new FlowPanel();

            if(beanList.size() == 0) {
                tabContent.add(new Label(RDL.i18n.noDataToDisplay()));
            }
            tabPanel.add(tabContent, i+"");

        }

        tabPanel.setWidth("100%");
        //select first tab

        // creates checkboxes for reference type

        final String[] referenceTypes = new String[] {
                RDLConstants.ReferenceType.POSITIVE,
                RDLConstants.ReferenceType.NEGATIVE,
                RDLConstants.ReferenceType.NEUTRAL
        };

        final String[] referenceTypesText = new String[] {
                RDL.i18n.positive(),
                RDL.i18n.negative(),
                RDL.i18n.neutral()
        };

        final CheckBox[] checkBoxArray = new CheckBox[3];

        String[] checkedReferences = pReferenceTypes.split(",");

        for (int i=0; i<referenceTypes.length; i++) {
            checkBoxArray[i] = new CheckBox(referenceTypesText[i]);
            checkBoxArray[i].setStyleName("checkBoxBtn");

            for (int k=0; k<checkedReferences.length; k++) {
                if(checkedReferences[k].equals(referenceTypes[i])) {
                    checkBoxArray[i].setValue(true);
                }
            }

            // checkbox click handle, gets list of references by selected reference type, refresh a list
            checkBoxArray[i].addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    // get check box values that are checked (not only the current clicked checkbox)
                    String checkedFlags = getCheckedFlags(checkBoxArray, referenceTypes);

                    if(!checkedFlags.equals("")) {
                        loadingWidget.getElement().getStyle().setProperty("display","block");
                        presenter.getSnipReferences(checkedFlags, 0);
                    } else {
                        showReferences(new ArrayList<AutoBean<SnipBean>>(), "", 0);
                    }
                }
            });

            checkboxBtnParent.add(checkBoxArray[i]);

        }

        for (int j=0; j<beanList.size(); j++) {
            ReferenceListRow referenceListRow = new ReferenceListRow(beanList.get(j), currentUserBean);
            ((FlowPanel) tabPanel.getWidget(pageIndex)).add(referenceListRow);
        }
        tabPanel.selectTab(pageIndex);

        tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> integerBeforeSelectionEvent) {
                loadingWidget.getElement().getStyle().setProperty("display","block");
                String checkedFlags = getCheckedFlags(checkBoxArray, referenceTypes);
                presenter.getSnipReferences(checkedFlags, integerBeforeSelectionEvent.getItem());
            }
        });

        referenceListCont.add(tabPanel);



    }

    private String getCheckedFlags(CheckBox[] checkBoxArray, String[] referenceTypes) {
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
}
