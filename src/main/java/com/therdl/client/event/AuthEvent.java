package com.therdl.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.therdl.client.dto.UserDataProxy;

/**
 * authorization event, in response to logging into Google system
 * 
 */
public class AuthEvent extends GwtEvent<AuthEventHandler> {

  public static Type<AuthEventHandler> TYPE = new Type<AuthEventHandler>();

  public static enum Auth {

    /**
     * user was logged in
     */
    LOGGEDIN, 

    /**
     * user logged out 
     */
    LOGGEDOUT;
  }

  private Auth auth;

  private UserDataProxy userData;

  public AuthEvent(Auth auth, UserDataProxy userData) {
    this.auth = auth;
    this.userData = userData;
  }

  @Override
  public Type<AuthEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(AuthEventHandler handler) {
    handler.onAuthEvent(this);
  }

  public Auth getAuthEvent() {
    return auth;
  }

  public UserDataProxy getUserData() {
    return userData;
  }

}
