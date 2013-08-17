package com.therdl.client.view.impl;


import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SignInView;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.WidgetHolder;
import com.therdl.shared.Messages;
import com.therdl.shared.beans.AuthUserBean;

public class WelcomeViewImpl extends Composite implements WelcomeView{

	private Presenter presenter;
    private HTMLPanel menuPanel;
    private FlowPanel contentPanel;
    private SignInView signInView;
    private AutoBean<AuthUserBean> authnBean;
    private Messages messages;
    private AppMenu appMenu;

	public WelcomeViewImpl(Messages messages, AutoBean<AuthUserBean> authnBean) {
        this.authnBean = authnBean;
        this.messages = messages;
	    //add the menu
	    menuPanel = new HTMLPanel("<div class='navbar navbar-inverse navbar-fixed-top'></div>");
        appMenu = (AppMenu) WidgetHolder.getInstance().getAppMenu();

	    menuPanel.add(appMenu);
	    
	    //add splash content
	    HTMLPanel panel = setSplashScreen(messages);

		contentPanel = new FlowPanel();
		contentPanel.add(menuPanel);
        if (!authnBean.as().isAuth()) {
            signInView = new SignInViewImpl(this);
            HTMLPanel headerPanel =  setloginheader(messages);
            contentPanel.add(headerPanel);
            contentPanel.add(signInView);

        } else  	contentPanel.add(panel);

		initWidget(contentPanel);
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


    public  void  setloginresult(String name, String email, boolean auth) {
      if (auth) {
          contentPanel.clear();
          contentPanel.add(menuPanel);
          contentPanel.add(setSplashScreen(messages));
          appMenu.setUser(name);
          appMenu.setEmail(email);
      }

    }


    private   HTMLPanel  setSplashScreen(Messages messages) {

        HTMLPanel panel = new HTMLPanel("");
        panel.add(new HTML("<h3 class = 'brand'>" + messages.mainWelcome() + "</h3>"));
        panel.add(new HTML("<div class = 'row span3'><div class = 'text'>" + messages.mainSubtitle1() + "</div></div>"));
        panel.add(new HTML("<div class = 'row span3'><div class = 'text'>" + messages.mainSubtitle2() +messages.mainSubtitle3()+"</div></div>"));
        panel.add(new HTML("<div class = 'row span3'><div class = 'text'>" +messages.mainSubtitle4()+messages.mainSubtitle5() + "</div></div>"));
        panel.setStyleName("container");
        return panel;
    }

    private   HTMLPanel  setloginheader(Messages messages) {

        HTMLPanel panel = new HTMLPanel("<div class= 'container'>");
        panel.add(new HTML("<h3 class='brand'>" + messages.mainWelcome() + "</h3>"));
        panel.add(new HTML("<div class='row span4'><div class = 'text'>" + messages.mainSubtitle1() + "</div></div>"));
        panel.setStyleName("container");
        return panel;
    }

      public void logout() {

          contentPanel.clear();
          HTMLPanel panel = new HTMLPanel("");
          panel.add(new HTML("<h3 class = 'brand'>Good bye please close browser for your security</h3>"));
          contentPanel.add(panel);
      }

}
