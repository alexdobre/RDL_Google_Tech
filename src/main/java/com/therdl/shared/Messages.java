package com.therdl.shared;

import com.google.gwt.i18n.client.Constants;

public interface Messages extends Constants{
	
	@Key("main.submit")
	String mainSubmit();
	
	@Key("main.rate")
	String mainRate();
	
	@Key("main.browse")
	String mainBrowse();
	
	@Key("main.offer.serv")
	String mainOfferServ();
	
	@Key("main.rate.serv")
	String mainRateServ();
	
	@Key("main.groups")
	String mainGroups();
	
	@Key("main.activities")
	String mainActivities();

	@Key("main.profile")
	String mainProfile();
	
	@Key("main.enter")
	String mainEnter();
	
	@Key("main.welcome")
	String mainWelcome();

	@Key("main.subtitle1")
	String mainSubtitle1();
	
	@Key("main.subtitle2")
	String mainSubtitle2();
	
	@Key("main.subtitle3")
	String mainSubtitle3();
	
	@Key("main.subtitle4")
	String mainSubtitle4();
	
	@Key("main.subtitle5")
	String mainSubtitle5();
	
	@Key("error.image.load")
	String errorImageLoad();
	
	@Key("alt.rdl.main.image")
	String altRdlMainImage(); 
	

	String page_rdl_tree_overview();	
	String page_rdl_tree_compatibility();
	String page_rdl_tree_emotional_support();
	String page_rdl_tree_attraction();
	String page_rdl_tree_seduction(); 
	String page_rdl_tree_tendencies();
	String page_rdl_tree_affairs();
}
