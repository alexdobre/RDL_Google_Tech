package com.therdl.client.view.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Layout extends Composite {

  private static LayoutUiBinder uiBinder = GWT.create(LayoutUiBinder.class);
  @UiField SimplePanel pContent;
  @UiField FlowPanel adPanel;
  @UiField VerticalPanel vpWidget;
  @UiField VerticalPanel vpFooter;
  @UiField VerticalPanel vpMain;
  @UiField FlowPanel fpPlusOne;
  @UiField HTML htmlFacebook;
  @UiField HTML htmlVersion;
  @UiField HTML htmlAbout;
  @UiField HTML htmlCopyright;
  


  interface LayoutUiBinder extends UiBinder<Widget, Layout> {
  }

  public Layout() {
    initWidget(uiBinder.createAndBindUi(this));
    
    drawPlusOne();
    
    drawFacebook();
    
    drawVersion();
    
    drawCopyright();
  }


  public SimplePanel getContentPanel() {
    return pContent;
  }
  
  /**
   * moving a few seconds later, for the effect only
   */
  private void moveAdsDivTimed() {
    Timer t = new Timer() {
      public void run() {
        moveAdsDiv();
      }
    };
    t.schedule(500);
  }
  
  /**
   * move the ads div to the better location in the app layout
   */
  public void moveAdsDiv() {
    RootPanel w = RootPanel.get("ads");
    adPanel.add(w);
  }
  
  private void drawPlusOne() {
    String s = "<g:plusone href=\"https://mywalletinventory.appspot.com\"></g:plusone>";
    HTML h = new HTML(s);
    fpPlusOne.add(h);
    
    Document doc = Document.get();
    ScriptElement script = doc.createScriptElement();
    script.setSrc("https://apis.google.com/js/plusone.js");
    script.setType("text/javascript");
    script.setLang("javascript");
    doc.getBody().appendChild(script);
  }

  private void drawFacebook() {
    String s = "<div class=\"fb-like\" data-href=\"https://mywalletinventory.appspot.com\" data-send=\"false\" data-layout=\"button_count\" data-show-faces=\"true\"></div><div id=\"fb-root\"></div>";
    htmlFacebook.setHTML(s);
        
    Document doc = Document.get();
    ScriptElement script = doc.createScriptElement();
    script.setSrc("https://connect.facebook.net/en_US/all.js#xfbml=1&appId=158427050894091");
    script.setType("text/javascript");
    script.setLang("javascript");
    doc.getBody().appendChild(script);
  }
  
  private void drawVersion() {
    String s = "version 0.1";
    htmlVersion.setHTML(s);
  }
  
  private void drawCopyright() {
    String s = "\u00A9 FreedomCorp LTD.&nbsp;&nbsp;";
    htmlCopyright.setHTML(s);
  }
  
}
