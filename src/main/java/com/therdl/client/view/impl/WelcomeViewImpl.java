package com.therdl.client.view.impl;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.events.*;

import java.util.logging.Logger;

/**
 * WelcomeViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the landing page
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ SignInViewImpl signInView, login pop up widget
 * @ AppMenu appMenu the upper menu view
 * fields below are standard GWT UIBinder display elements
 * @ FocusPanel  IdeasButton, StoriesButton, VoteButton, ServicesButton,
 * FocusPanel widgets allow complex events such as 'hover'
 * @ SpanElement hoverDiv this is the feedback for the hover text from the  FocusPanel widgets
 * @ Image logo, logo image, note java does not support transparency layers
 * @ AutoBean<CurrentUserBean> currentUser  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * maintains client side state
 */
public class WelcomeViewImpl extends Composite implements WelcomeView {

    private static Logger log = Logger.getLogger("");


    interface WelcomeViewImplUiBinder extends UiBinder<Widget, WelcomeViewImpl> {
    }

    private static WelcomeViewImplUiBinder uiBinder = GWT.create(WelcomeViewImplUiBinder.class);


    private Presenter presenter;

    private SignInViewImpl signInView;

    private AutoBean<CurrentUserBean> currentUser;

    @UiField
    AppMenu appMenu;

    @UiField
    Image logo;

    @UiField
    FocusPanel IdeasButton;

    @UiField
    FocusPanel StoriesButton;

    @UiField
    FocusPanel VoteButton;

    @UiField
    FocusPanel ServicesButton;


    @UiField
    SpanElement hoverDiv;


    public WelcomeViewImpl(AutoBean<CurrentUserBean> currentUser) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUser = currentUser;
        //  appMenu.setUserInfoVisible(false);
        appMenu.setLogOutVisible(false);
        appMenu.setLogOutVisible(false);
        appMenu.setMainGroupVisible(true);
        logo.setStyleName("splashLogo");
        IdeasButton.setSize("100px", "40px");
        IdeasButton.getElement().getStyle().setProperty("margin", "10px");
        IdeasButton.getElement().getStyle().setProperty("padding", "10px");
        StoriesButton.setSize("100px", "40px");
        StoriesButton.getElement().getStyle().setProperty("margin", "10px");
        StoriesButton.getElement().getStyle().setProperty("padding", "10px");
        VoteButton.setSize("100px", "40px");
        VoteButton.getElement().getStyle().setProperty("margin", "10px");
        VoteButton.getElement().getStyle().setProperty("padding", "10px");
        ServicesButton.setSize("100px", "40px");
        ServicesButton.getElement().getStyle().setProperty("margin", "10px");
        ServicesButton.getElement().getStyle().setProperty("padding", "10px");

        appMenu.setSignUpVisible(true);

        GuiEventBus.EVENT_BUS.addHandler(LogInEvent.TYPE, new LogInEventEventHandler() {

            @Override
            public void onLogInEvent(LogInEvent onLoginEvent) {
                showLoginPopUp();
            }
        });

    }

    /**
     * show the signInView, login pop up widget  and set its parameters
     */
    public void showLoginPopUp() {

        signInView = new SignInViewImpl(this);
        signInView.setGlassEnabled(true);
        signInView.setModal(true);
        signInView.setPopupPosition(20, 30);
        signInView.show();
        signInView.getLoginFail().setVisible(false);


    }

    /**
     * show the error validation message in the signInView
     */
    @Override
    public void showLoginFail() {

        signInView.getLoginFail().setVisible(true);
        signInView.getLoginFail().setText("Login Fails please check your credentials");


    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        log.info("WelcomeViewImpl setPresenter");


    }


    /**
     * Sets the upper header Menu to the correct state for supplied credentials
     * post sign up called from presenter
     *
     * @param name  supplied credential
     * @param email supplied credential
     * @param auth  boolean auth state from server via presenter
     */
    @Override
    public void setloginresult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true " + name);

            this.appMenu.setLogOutVisible(true);
            this.appMenu.setSignUpVisible(false);
            this.appMenu.setUserInfoVisible(true);
            this.appMenu.setUser(name);
            this.appMenu.setEmail(email);
            this.appMenu.setLogInVisible(false);
        } else {
            this.appMenu.setLogOutVisible(false);
            this.appMenu.setSignUpVisible(true);
            this.appMenu.setUserInfoVisible(false);
            this.appMenu.setLogInVisible(true);
        }

    }


    /**
     * reset app menu on log out
     */
    public void logout() {

        appMenu.setLogOutVisible(false);
        appMenu.setSignUpVisible(true);
        appMenu.setUserInfoVisible(false);
        appMenu.setLogInVisible(true);

    }

    /**
     * called form signin view pop up to initiate log in flow
     *
     * @param emailtxt     supplied credential
     * @param passwordText supplied credential
     */
    public void onSubmit(String emailtxt, String passwordText) {

        presenter.doLogIn(emailtxt, passwordText);
    }

    @Override
    public AppMenu getAppMenu() {
        return appMenu;
    }


    /**
     * displays the hover tooltip for this widget
     *
     * @param event Standard GWT hover event
     */
    @UiHandler("IdeasButton")
    public void onMouseOver(MouseOverEvent event) {
        hoverDiv.setInnerHTML(Constants.IDEAS_TEXT);
    }

    /**
     * displays the hover tooltip for this widget
     *
     * @param event Standard GWT hover event
     */
    @UiHandler("StoriesButton")
    public void onMouseOver1(MouseOverEvent event) {
        hoverDiv.setInnerHTML(Constants.STORIES_TEXT);
    }

    /**
     * displays the hover tooltip for this widget
     *
     * @param event Standard GWT hover event
     */
    @UiHandler("VoteButton")
    public void onMouseOver2(MouseOverEvent event) {
        hoverDiv.setInnerHTML(Constants.VOTES_TEXT);
    }

    /**
     * displays the hover tooltip for this widget
     *
     * @param event Standard GWT hover event
     */
    @UiHandler("ServicesButton")
    public void onMouseOver3(MouseOverEvent event) {
        hoverDiv.setInnerHTML(Constants.SERVICES_TEXT);
    }


}

