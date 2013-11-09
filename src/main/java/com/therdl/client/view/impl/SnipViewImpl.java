package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.therdl.client.view.ProfileView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.widget.ReferenceListRow;
import com.therdl.client.view.widget.SnipListRow;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
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
    FlowPanel snipViewCont, referenceCont, radioBtnParent, referenceListCont;
    @UiField
    RichTextArea richTextArea, richTextAreaRef;
    @UiField
    Button showRef, leaveRef, saveRef, closeRef;
    @UiField
    RadioButton rb1, rb2, rb3;
    @UiField
    HTMLPanel rootPanel;

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

    /**
     * creates main gwt widgets of snip view page
     * @param snipBean snip as SnipBean object
     */
    public void viewSnip(AutoBean<SnipBean> snipBean) {
        this.currentSnipBean = snipBean;
        referenceListCont.clear();
        snipViewCont.clear();

        // this is the top widget, like in the list widget
        SnipListRow snipListRow = new SnipListRow(snipBean, currentUserBean);
        snipViewCont.add(snipListRow);
        richTextArea.setHTML(snipBean.as().getContent());
        leaveRef.getElement().getStyle().setProperty("marginLeft", "10px");

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
        presenter.getSnipReferences();
    }

    /**
     * saves a reference
     * constructs bean object for reference and calls presenter's function to save created reference
     * @param event
     */
    @UiHandler("saveRef")
    public void onSaveRefClicked(ClickEvent event) {
        if(richTextAreaRef.getText().equals("")) {
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
        newBean.as().setContent(richTextAreaRef.getText());
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
    public void showReferences(ArrayList<AutoBean<SnipBean>> beanList) {
        referenceListCont.clear();
        referenceListCont.getElement().getStyle().setProperty("display", "block");
        referenceCont.getElement().getStyle().setProperty("display", "none");

        // default size of rows in one tab
        int listRowSize = Constants.DEFAULT_REFERENCE_PAGE_SIZE;
        int tabCount = (int) Math.ceil((double)beanList.size()/listRowSize);

        TabPanel tabPanel = new TabPanel();

        // creates tabs of count tabCount
        for (int i=1; i<=tabCount; i++) {
            // creates content of current tab
            FlowPanel tabContent = new FlowPanel();
            int startIndex = (i-1)*listRowSize;

            int currentIndex = startIndex;
            for (int j=0; j<listRowSize; j++) {
                if(currentIndex >= beanList.size())
                    break;

                ReferenceListRow referenceListRow = new ReferenceListRow(beanList.get(currentIndex), currentUserBean);

                tabContent.add(referenceListRow);
                currentIndex++;
            }

            tabPanel.add(tabContent, i+"");

        }

        tabPanel.setWidth("100%");
        //select first tab
        tabPanel.selectTab(0);
        referenceListCont.add(tabPanel);

    }
}
