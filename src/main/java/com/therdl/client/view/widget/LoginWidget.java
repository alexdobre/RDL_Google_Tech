package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class LoginWidget extends Composite {

  private static LoginWidgetUiBinder uiBinder = GWT.create(LoginWidgetUiBinder.class);
  @UiField HTML htmlNick;
  @UiField HTML htmlUrl;
  @UiField Image imgHelp;
  @UiField HTML htmlPlusOne;
  
 // private ClientFactory clientFactory;

  private boolean alreadyInit;
  

  interface LoginWidgetUiBinder extends UiBinder<Widget, LoginWidget> {
  }

  public LoginWidget() {
    initWidget(uiBinder.createAndBindUi(this));

    imgHelp.setSize("16px", "16px");
    
    addStyleName("app-loginwidget");
    
    drawPlusOne();
  }


  private void createUser() {


  }


  private void drawHelp() {
  }
  
  private void drawPlusOne() {
    String s = "<g:plusone href=\"https://mywalletinventory.appspot.com\"></g:plusone>";
    htmlPlusOne.setHTML(s);
  }

  @UiHandler("imgHelp")
  void onImgHelpClick(ClickEvent event) {
    drawHelp();
  }
  @UiHandler("imgHelp")
  void onImgHelpTouchStart(TouchStartEvent event) {
    drawHelp();
  }
  @UiHandler("imgHelp")
  void onImgHelpMouseOver(MouseOverEvent event) {
    drawHelp();
  }
  
}
