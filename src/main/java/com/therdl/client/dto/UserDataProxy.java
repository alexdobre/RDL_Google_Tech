package com.therdl.client.dto;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.therdl.server.model.UserData;


public interface UserDataProxy extends EntityProxy {

  String getLoginUrl();
  
  String getLogoutUrl();
  
  String getId();

  String getGoogleNickname();
  
}
