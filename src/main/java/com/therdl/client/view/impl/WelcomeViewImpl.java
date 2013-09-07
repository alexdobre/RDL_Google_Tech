package com.therdl.client.view.impl;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SignInView;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.WidgetHolder;
import com.therdl.shared.Messages;
import com.therdl.shared.beans.AuthUserBean;

import java.util.logging.Logger;

public class WelcomeViewImpl extends Composite implements WelcomeView  {

    private static Logger log = Logger.getLogger("");


    interface WelcomeViewImplUiBinder extends UiBinder<Widget, WelcomeViewImpl> {
    }

    private static WelcomeViewImplUiBinder uiBinder = GWT.create(WelcomeViewImplUiBinder.class);



	private Presenter presenter;

    private SignInView signInView;
    private AutoBean<AuthUserBean> authnBean;

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




    public WelcomeViewImpl(Messages messages, AutoBean<AuthUserBean> authnBean) {
        initWidget(uiBinder.createAndBindUi(this));
        appMenu.setUserInfoVisible(false);
        appMenu.setLogOutVisible(false);
        appMenu.setLogOutVisible(false);
        appMenu.setMainGroupVisible(true);
        logo.setStyleName("splashLogo");
        IdeasButton.setSize("100px","40px");
        IdeasButton.getElement().getStyle().setProperty("margin", "10px");
        IdeasButton.getElement().getStyle().setProperty("padding", "10px");
        StoriesButton.setSize("100px","40px");
        StoriesButton.getElement().getStyle().setProperty("margin", "10px");
        StoriesButton.getElement().getStyle().setProperty("padding", "10px");
        VoteButton.setSize("100px","40px");
        VoteButton.getElement().getStyle().setProperty("margin", "10px");
        VoteButton.getElement().getStyle().setProperty("padding", "10px");
        ServicesButton.setSize("100px","40px");
        ServicesButton.getElement().getStyle().setProperty("margin", "10px");
        ServicesButton.getElement().getStyle().setProperty("padding", "10px");
        this.authnBean = authnBean;

        signInView = new SignInViewImpl(this);
        signInView.getLoginFail().setVisible(false);


        if (!authnBean.as().isAuth()) {
            log.info("WelcomeViewImpl constructor: !authnBean.as().isAuth()" );



        } else  {
            if( signInView != null )
            log.info("WelcomeViewImpl constructor: else signInView.setSignIsVisible(false)" );


        }

	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
        log.info("WelcomeViewImpl setPresenter" );



	}


    public  void  setloginresult(String name, String email, boolean auth) {
      if (auth) {
          log.info("WelcomeViewImpl setloginresult auth true" );


          appMenu.setUser(name);
          appMenu.setEmail(email);
          appMenu.setLogOutVisible(true);
          appMenu.setSignUpVisible(false);
          appMenu.setUserInfoVisible(true);
      }

    }



      public void logout() {


      }



    public SignInView getSignInView() {

        return this.signInView;


    }


    public void onSubmit() {

        presenter.doLogIn();
    }

    @Override
    public AppMenu getAppMenu() {
        return appMenu;
    }





    @UiHandler("IdeasButton")
    public void onMouseOver(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(ideas);
    }



    @UiHandler("StoriesButton")
    public void onMouseOver1(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(stories);
    }


    @UiHandler("VoteButton")
    public void onMouseOver2(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(votes);
    }


    @UiHandler("ServicesButton")
    public void onMouseOver3(MouseOverEvent event)
    {
        hoverDiv.setInnerHTML(services);
    }

    // hover data

    static final String ideas = "The RDL Snips (Information snippets) is a concept of a small piece of golden concise thought provoking and useful information. Text snips, video snips and audio.";
    static final String  stories = "You need advice specific for your problem. You need the experience of people who went through what you are going through. You need the support of people who truly understand your situation. Discussion tags and streams are for you. ";
    static final String votes = "With this we hope to achieve an unparalleled amount of transparency and feedback. A site that truly is for the users and we work for them and report to them. ";
    static final String services=  "The intent of this section is to promote high quality counselling services. Registered members can vote on the service or counsellor along with references. \n";
}

