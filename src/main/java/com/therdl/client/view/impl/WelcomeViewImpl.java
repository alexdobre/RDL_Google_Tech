package com.therdl.client.view.impl;


import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.LogoutPopupWidget;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.client.view.widget.text.*;
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
    FocusPanel compatibilityCat;
    @UiField
    FocusPanel connectionCat;
    @UiField
    FocusPanel exteriorCat;
    @UiField
    FocusPanel eroticismCat;
    @UiField
    FocusPanel seductionCat;
    @UiField
    FocusPanel psyTendCat;
    @UiField
    FocusPanel affairsCat;
    @UiField
    FocusPanel abuseCat;

    @UiField
    com.github.gwtbootstrap.client.ui.Button welcomeVideoButton;

    LogoutPopupWidget logoutPopup;


    public WelcomeViewImpl(AutoBean<CurrentUserBean> currentUser) {
        initWidget(uiBinder.createAndBindUi(this));
        this.currentUser = currentUser;
        //  appMenu.setUserInfoVisible(false);
        appMenu.setLogOutVisible(false);
        appMenu.setMainGroupVisible(true);
        logo.setStyleName("splashLogo");

        appMenu.setSignUpVisible(true);

        GuiEventBus.EVENT_BUS.addHandler(LogInEvent.TYPE, new LogInEventEventHandler() {

            @Override
            public void onLogInEvent(LogInEvent onLoginEvent) {
                showLoginPopUp(20,30, "");
            }
        });

    }

    /**
     * creates and shows login popup and sets its parameters
     * @param posLeft left position
     * @param posTop top position
     * @param pageToRedirect page to redirect if after login is completed successfully
     */
    public void showLoginPopUp(int posLeft, int posTop, String pageToRedirect) {

        signInView = new SignInViewImpl(this, pageToRedirect);
        signInView.setGlassEnabled(true);
        signInView.setModal(true);
        signInView.setPopupPosition(posLeft, posTop);
        signInView.show();
        signInView.getLoginFail().setVisible(false);

    }

    /**
     * shows log out popup with message
     */
    public void showLogoutPopUp() {
        if(logoutPopup == null) {
            logoutPopup = new LogoutPopupWidget();
            logoutPopup.setGlassEnabled(true);
            logoutPopup.setModal(true);

            logoutPopup.setWidth("200px");
            logoutPopup.setHeight("100px");
        }
        logoutPopup.center();
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
        showLogoutPopUp();

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

    public void init(){
    }

    /**
     * Loads the introduction video
     */
    @UiHandler("welcomeVideoButton")
    public void onClickVideoIntro(ClickEvent event) {
        //we initialize the welcome video player
        AbstractMediaPlayer player = null;
        try {
            // create the player, specifying URL of media
            player = new YouTubePlayer("wEJ40eqFBeo","100%", "350px");

            DialogBox dialog = new DialogBox(true,false);

            dialog.ensureDebugId("cwBasicPopup-simplePopup");
            dialog.setAnimationEnabled(true);
            dialog.setWidth("450px");
            dialog.setWidget(player);

            // Reposition the popup relative to the button
            Widget source = (Widget) event.getSource();
            int left = source.getAbsoluteLeft() + 10;
            int top = source.getAbsoluteTop() + 10;
            dialog.setPopupPosition(left, top);


            dialog.show();
        } catch(PluginVersionException e) {
            constructPopup(new HTML(".. please download the necessary plugin.."),event).show();
        } catch(PluginNotFoundException e) {
            // catch PluginNotFoundException and display a friendly notice.
            constructPopup(new HTML(".. plugin not found, please download the necessary plugin to run YouTube .."),event).show();
        }
    }



    /**
     * displays the on click popup description
     *
     * @param event Standard GWT hover event
     */
    @UiHandler("compatibilityCat")
    public void onCompatibilityCattClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new CompatibilityDescription(),event);
        simplePopup.show();
    }
    @UiHandler("connectionCat")
    public void onConnectionCatClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new ConnectionDescription(),event);
        simplePopup.show();
    }
    @UiHandler("exteriorCat")
    public void onExteriorCatClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new ExteriorDescription(),event);
        simplePopup.show();
    }
    @UiHandler("eroticismCat")
    public void onEroticismCatClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new EroticismDescription(),event);
        simplePopup.show();
    }
    @UiHandler("seductionCat")
    public void onSeductionCatClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new SeductionDescription(),event);
        simplePopup.show();
    }
    @UiHandler("psyTendCat")
    public void onPsyTendCatClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new PsyTendDescription(),event);
        simplePopup.show();
    }
    @UiHandler("affairsCat")
    public void onAffairsCatClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new AffairsDescription(),event);
        simplePopup.show();
    }
    @UiHandler("abuseCat")
    public void onAbuseCatClick(ClickEvent event) {
        DecoratedPopupPanel simplePopup = constructPopup(new AbuseDescription(),event);
        simplePopup.show();
    }

    private DecoratedPopupPanel constructPopup(Widget widget,ClickEvent event){
        final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
        simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
        simplePopup.setAnimationEnabled(false);
        simplePopup.setWidth("800px");
        simplePopup.setWidget(widget);

        // Reposition the popup relative to the button
        Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft() + 10;
        int top = source.getAbsoluteTop() + 10;
        simplePopup.setPopupPosition(left, top);

        return simplePopup;
    }
}

