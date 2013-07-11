package com.therdl.client.view.impl;


import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.widget.WidgetHolder;
import com.therdl.shared.Messages;

public class WelcomeViewImpl extends Composite implements WelcomeView{

	private Presenter presenter;
	
	public WelcomeViewImpl(EventBus eventBus,Messages messages) {

	    
	    //add the menu
	    HTMLPanel menuPanel = new HTMLPanel("<div></div>");
	    menuPanel.add(WidgetHolder.getInstance().getAppMenu());
	    
	    //add the welcome text
	    HTMLPanel mainPanel = new HTMLPanel("<div></div>");
	    mainPanel.add(new HTML("<h1>" + messages.mainWelcome() + "</h1>"));
		mainPanel.add(new HTML("<h3>" + messages.mainSubtitle1() + "</h3>"));
		mainPanel.add(new HTML("<h3>" + messages.mainSubtitle2() + "</h3>"));
		mainPanel.add(new HTML("<h3>" + messages.mainSubtitle3() + "</h3>"));
		mainPanel.add(new HTML("<h3>" + messages.mainSubtitle4() + "</h3>"));
		mainPanel.add(new HTML("<h3>" + messages.mainSubtitle5() + "</h3>"));
		
		//wrap it nicely
		HTMLPanel contentPanel = new HTMLPanel("<div></div>");
		contentPanel.add(menuPanel);
		contentPanel.add(mainPanel);
		initWidget(contentPanel);
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
