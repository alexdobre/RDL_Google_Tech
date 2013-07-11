package com.therdl.server.model;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.logging.Logger;



public class UserData {


  private static final Logger log = Logger.getLogger(UserData.class.getName());

  private static User getGoogleUser() {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn() == false) {
      return null;
    }
    User user = userService.getCurrentUser();
    return user;
  }

  public static Long getLoggedInUserId() {
     Long  id = new Long(1l);

    return  id;
  }

  public static UserData findUserData(String id) {
    if (id == null) {
      return null;
    }

    return null;
  }

  public static UserData findUserDataByGoogleEmail(String googleEmail) {
//    PersistenceManager pm = getPersistenceManager();
//    try {
//      javax.jdo.Query query = pm.newQuery("select from " + UserData.class.getName());
//      query.setRange(0, 1);
//      query.setFilter("googleEmail==\""+ googleEmail + "\"");
//      List<UserData> list = (List<UserData>) query.execute();
//      int size = list.size();
//      if (size == 0) {
//        return null;
//      }
//      Iterator<UserData> itr = list.iterator();
//      UserData ud = itr.next();
//      return ud;
//    } catch (Exception e) {
//      log.log(Level.SEVERE, "Error: UserData.findUserDataByGoogleEmail(googleEmail): googleEmail=" + googleEmail, e);
//      e.printStackTrace();
//    } finally {
//      pm.close();
//    }
    return null;
  }

  public static UserData findUserDataByGoogleUserId(String googleUserId) {
//    PersistenceManager pm = getPersistenceManager();
//    try {
//      javax.jdo.Query query = pm.newQuery("select from " + UserData.class.getName());
//      query.setRange(0, 1);
//      query.setFilter("googleUserId==\"" + googleUserId + "\"");
//      List<UserData> list = (List<UserData>) query.execute();
//      int size = list.size();
//      if (size == 0) {
//        return null;
//      }
//      Iterator<UserData> itr = list.iterator();
//      UserData ud = itr.next();
//      return ud;
//    } catch (Exception e) {
//      log.log(Level.SEVERE, "Error: UserData.findUserDataByGoogleUserId(googleUserId): googleUserId=" + googleUserId, e);
//      e.printStackTrace();
//    } finally {
//      pm.close();
//    }
      return null;
  }


  private Long version;

  private String googleUserId;


  private String googleEmail;


  private String googleNickname;


  private String loginUrl;


  private String logoutUrl;
  
 
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("version=" + version + ",");
    sb.append("googleUserId=" + googleUserId + ",");
    sb.append("googleEmail=" + googleEmail + ",");
    sb.append("googleNickname=" + googleNickname + ",");
    sb.append("loginUrl=" + loginUrl + ",");
    sb.append("logoutUrl=" + logoutUrl + " ");
    return sb.toString();
  }

  public void setId(String id) {
    if (id == null) {
      return;
    }

  }
  public String getId() {
    String id = null;

    return id;
  }



  public void setVersion(Long version) {
    this.version = version;
  }
  public Long getVersion() {
    if (version == null) {
      version = 0l;
    }
    return version;
  }

  public void setGoogleUserId(String googleUserId) {
    this.googleUserId = googleUserId;
  }
  public String getGoogleUserId() {
    return googleUserId;
  }

  public void setGoogleEmail(String googleEmail) {
    if (googleEmail != null) {
      googleEmail = googleEmail.toLowerCase();
    }
    this.googleEmail = googleEmail;
  }
  public String getGoogleEmail() {
    return googleEmail;
  }

  public void setGoogleNickname(String googleNickname) {
    this.googleNickname = googleNickname;
  }
  public String getGoogleNickname() {
    return googleNickname;
  }

  public void setLoginUrl() {
    UserService userService = UserServiceFactory.getUserService();
    loginUrl = userService.createLoginURL("/");
  }
  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLogoutUrl() {
    UserService userService = UserServiceFactory.getUserService();
    logoutUrl = userService.createLogoutURL("/");
  }
  public String getLogoutUrl() {
    return logoutUrl;
  }

  public static UserData createUserData() {
    UserData userData = null; 

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn() == false) {
      UserData r = new UserData();
      r.setLoginUrl();
      //r.setLogoutUrl(); // not needed
      return r;
    }

    User u = userService.getCurrentUser();
    if (u == null) {
      UserData r = new UserData();
      r.setLoginUrl();
      //r.setLogoutUrl(); // not needed
      return r;
    }

    // has the user been here before? lookup and or create
    userData = findUserDataByGoogleUserId(u.getUserId());
    if (userData == null) {
      userData = new UserData();
    }

    userData.setGoogleUserId(u.getUserId());
    userData.setGoogleEmail(u.getEmail());
    userData.setGoogleNickname(u.getNickname());
    //userData.setLoginUrl(); // not needed
    userData.setLogoutUrl();

    return userData.persist();
  }

  public UserData persist() {

    // JPA does this automatically, but JDO won't. Not sure why.
    if (version == null) {
      version = 0l;
    }
    version++;

//    PersistenceManager pm = getPersistenceManager();
//    Transaction tx = pm.currentTransaction();
//    try {
//      tx.begin();
//      pm.makePersistent(this);
//      tx.commit();
//    } catch (Exception e) {
//      log.log(Level.SEVERE, "Error: UserData.persist(): this=" + this, e);
//      e.printStackTrace();
//      return null;
//    } finally {
//      if (tx.isActive()) {
//        tx.rollback();
//      }
//      pm.close();
//    }
    return this;
  }

   public void remove() {

    Long uid = getLoggedInUserId();

      return;
    }

//    PersistenceManager pm = getPersistenceManager();
//    Transaction tx = pm.currentTransaction();
//    try {
//      tx.begin();
//      pm.deletePersistent(this);
//      tx.commit();
//    } catch (Exception e) {
//      log.log(Level.SEVERE, "Error: UserData.remove(): this=" + this, e);
//      e.printStackTrace();
//    } finally {
//      if (tx.isActive()) {
//        tx.rollback();
//      }
//      pm.close();
//    }
  }
  
