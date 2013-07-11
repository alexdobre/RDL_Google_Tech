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
import com.therdl.client.dto.UserDataProxy;

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

//  public void setClientFactory(ClientFactory clientFactory) {
//    this.clientFactory = clientFactory;
//    
//    createUser();
//  }
  
  /**
   * this will create/lookup a user in the datastore according to the Google Login
   *  (if logged in or has logged in)
   */
  private void createUser() {
//    Request<UserDataProxy> req = clientFactory.getRequestFactory().getUserDataRequest().createUserData();
//    req.fire(new Receiver<UserDataProxy>() {
//      public void onSuccess(UserDataProxy data) {
//        process(data);
//      }
//      public void onFailure(ServerFailure error) {
//        super.onFailure(error);
//      }
//    });
  }

  private void process(UserDataProxy userData) {
    if (userData != null && 
        userData.getId() != null && 
        userData.getId().matches(".*([0-9]+).*") == true) {
      setLoggedIn(userData);
    } else {
      setLoggedOut(userData);
    }
    
    // tell the rest of the app a login event has happened
  //  clientFactory.setUserData(userData);
  }

  /**
   * lets use the url to show where to login at
   * @param userData
   */
  private void setLoggedOut(UserDataProxy userData) {
    if (userData == null) {
      // this shouldn't happen, b/c we need the urls
      return;
    }

    String url = userData.getLoginUrl();
    String qs = Window.Location.getQueryString();
    String token = History.getToken();
    if (qs != null) {
      url += URL.encode(qs);
    }
    if (token != null && token.length() > 0) {
      url += URL.encode("#" + token);
    }
    
    // This is a must, always clean before draw
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    builder.appendHtmlConstant("<a href='" + url + "'>")
    .appendEscaped("Sign In")
    .appendHtmlConstant("</a>");
    htmlUrl.setHTML(builder.toSafeHtml());
  }

  /**
   * logged in, lets go to the wallet list
   */
  private void setLoggedIn(UserDataProxy userData) {
    if (userData == null) {
      return;
    }

    setNick(userData);

    String url = userData.getLogoutUrl();
    String qs = Window.Location.getQueryString();
    if (qs != null) {
      url += URL.encode(qs);
    }

    // This is a must, always clean before draw
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    builder.appendHtmlConstant("<a href='" + url + "'>")
    .appendEscaped("Sign Out")
    .appendHtmlConstant("</a>");
    htmlUrl.setHTML(builder.toSafeHtml());
  }

  private void setNick(UserDataProxy userData) {
    if (userData == null) {
      return;
    }

    String nick = userData.getGoogleNickname();

    // This is a must, always clean before draw
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    builder.appendEscaped(nick);

    htmlNick.setHTML(builder.toSafeHtml());
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
