package com.therdl.shared;

/**
 * All shared constants could be placed here
 *  @DEPLOY   boolean determine url for deployment mode
 */
public class Constants {

    /**
     * Please change the DEPLOY constant to configure the paths of the
     * projects deployment for jetty or jboss
     *
     * true  : will optimize the paths for jboss
     * false : will optimize the paths for maven
     */
      public static final boolean DEPLOY = false;
  //  public static final boolean DEPLOY =  true;
      public static final int DEFAULT_PAGE_SIZE = 10;
}
