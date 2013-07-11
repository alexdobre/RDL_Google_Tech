package com.therdl.client.dto;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.therdl.server.model.UserData;

@ProxyFor(UserData.class)
public interface UserDataProxy extends EntityProxy {

  String getLoginUrl();
  
  String getLogoutUrl();
  
  String getId();

  String getGoogleNickname();
  
}
