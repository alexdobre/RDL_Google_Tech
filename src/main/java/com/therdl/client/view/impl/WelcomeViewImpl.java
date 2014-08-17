package com.therdl.client.view.impl;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.LogoutPopupWidget;
import com.therdl.client.view.widget.text.AbuseDescription;
import com.therdl.client.view.widget.text.AffairsDescription;
import com.therdl.client.view.widget.text.CompatibilityDescription;
import com.therdl.client.view.widget.text.ConnectionDescription;
import com.therdl.client.view.widget.text.EroticismDescription;
import com.therdl.client.view.widget.text.ExteriorDescription;
import com.therdl.client.view.widget.text.PsyTendDescription;
import com.therdl.client.view.widget.text.SeductionDescription;
import com.therdl.shared.beans.CurrentUserBean;

import java.util.logging.Logger;

/**
 * WelcomeViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the landing page
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ SignInViewImpl signInView, login pop up widget
 * fields below are standard GWT UIBinder display elements
 * @ FocusPanel  IdeasButton, StoriesButton, VoteButton, ServicesButton,
 * FocusPanel widgets allow complex events such as 'hover'
 * @ Image logo, logo image, note java does not support transparency layers
 * @ AutoBean<CurrentUserBean> currentUser  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * maintains client side state
 */
public class WelcomeViewImpl extends AppMenuView implements WelcomeView {

	private static Logger log = Logger.getLogger("");


	interface WelcomeViewImplUiBinder extends UiBinder<Widget, WelcomeViewImpl> {
	}

	private static WelcomeViewImplUiBinder uiBinder = GWT.create(WelcomeViewImplUiBinder.class);


	private Presenter presenter;

	private AutoBean<CurrentUserBean> currentUser;

	@UiField
	Image logo;
	@UiField
	org.gwtbootstrap3.client.ui.Button compatibilityCat;
	@UiField
	org.gwtbootstrap3.client.ui.Button connectionCat;
	@UiField
	org.gwtbootstrap3.client.ui.Button exteriorCat;
	@UiField
	org.gwtbootstrap3.client.ui.Button eroticismCat;
	@UiField
	org.gwtbootstrap3.client.ui.Button seductionCat;
	@UiField
	org.gwtbootstrap3.client.ui.Button psyTendCat;
	@UiField
	org.gwtbootstrap3.client.ui.Button affairsCat;
	@UiField
	org.gwtbootstrap3.client.ui.Button abuseCat;

	@UiField
	org.gwtbootstrap3.client.ui.Button welcomeVideoButton;

	LogoutPopupWidget logoutPopup;

	public WelcomeViewImpl(AutoBean<CurrentUserBean> currentUser, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUser = currentUser;
		logo.setStyleName("splashLogo");

		//core category buttons
		compatibilityCat.addStyleName("btn-cat");
		compatibilityCat.addStyleName("btn-cat-compat");
		connectionCat.addStyleName("btn-cat");
		connectionCat.addStyleName("btn-cat-conn");
		exteriorCat.addStyleName("btn-cat");
		exteriorCat.addStyleName("btn-cat-ext");
		eroticismCat.addStyleName("btn-cat");
		eroticismCat.addStyleName("btn-cat-ero");
		seductionCat.addStyleName("btn-cat");
		seductionCat.addStyleName("btn-cat-sed");
		psyTendCat.addStyleName("btn-cat");
		psyTendCat.addStyleName("btn-cat-psy");
		affairsCat.addStyleName("btn-cat");
		affairsCat.addStyleName("btn-cat-aff");
		abuseCat.addStyleName("btn-cat");
		abuseCat.addStyleName("btn-cat-abu");
	}

	/**
	 * shows log out popup with message
	 */
	public void showLogoutPopUp() {
		if (logoutPopup == null) {
			logoutPopup = new LogoutPopupWidget();
			logoutPopup.setGlassEnabled(true);
			logoutPopup.setModal(true);

			logoutPopup.setWidth("200px");
			logoutPopup.setHeight("100px");
		}
		logoutPopup.center();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		log.info("WelcomeViewImpl setPresenter");
	}

	/**
	 * displays the on click popup description
	 *
	 * @param event Standard GWT hover event
	 */
	@UiHandler("compatibilityCat")
	public void onCompatibilityCattClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new CompatibilityDescription(), event, 800);
		simplePopup.show();
	}

	@UiHandler("connectionCat")
	public void onConnectionCatClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new ConnectionDescription(), event, 800);
		simplePopup.show();
	}

	@UiHandler("exteriorCat")
	public void onExteriorCatClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new ExteriorDescription(), event, 800);
		simplePopup.show();
	}

	@UiHandler("eroticismCat")
	public void onEroticismCatClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new EroticismDescription(), event, 800);
		simplePopup.show();
	}

	@UiHandler("seductionCat")
	public void onSeductionCatClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new SeductionDescription(), event, 800);
		simplePopup.show();
	}

	@UiHandler("psyTendCat")
	public void onPsyTendCatClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new PsyTendDescription(), event, 800);
		simplePopup.show();
	}

	@UiHandler("affairsCat")
	public void onAffairsCatClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new AffairsDescription(), event, 800);
		simplePopup.show();
	}

	@UiHandler("abuseCat")
	public void onAbuseCatClick(ClickEvent event) {
		DecoratedPopupPanel simplePopup = ViewUtils.constructPopup(new AbuseDescription(), event, 800);
		simplePopup.show();
	}

}

